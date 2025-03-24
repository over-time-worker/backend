package com.owlexpress.hub.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.owlexpress.hub.application.dto.response.HubProductInfoResponseDto;
import com.owlexpress.hub.common.dto.response.PassportDto;
import com.owlexpress.hub.common.exception.HubException.HubNotFoundException;
import com.owlexpress.hub.common.exception.HubProductException;
import com.owlexpress.hub.common.exception.HubProductException.HubProductNotFoundException;
import com.owlexpress.hub.common.exception.HubProductException.HubProductNotFoundException.LockAcquisitionFailedException;
import com.owlexpress.hub.common.helper.HubHelper;
import com.owlexpress.hub.common.helper.PassportHelper;
import com.owlexpress.hub.common.util.GeoUtil;
import com.owlexpress.hub.domain.entity.Hub;
import com.owlexpress.hub.domain.entity.HubProduct;
import com.owlexpress.hub.domain.repository.HubProductRepository;
import com.owlexpress.hub.domain.repository.HubRepository;
import com.owlexpress.hub.infrastructure.client.OrderClient;
import com.owlexpress.hub.infrastructure.client.ProductClient;
import com.owlexpress.hub.presentation.dto.request.HubProductCreateRequestDto;
import com.owlexpress.hub.presentation.dto.request.OrderConfirmRequestDto;
import com.owlexpress.hub.presentation.dto.request.OrderConfirmRequestDto.Product;
import com.owlexpress.hub.presentation.dto.response.HubProductOrderConfirmResponseDto;
import com.owlexpress.hub.presentation.dto.response.HubProductOrderConfirmResponseDto.ConfirmedHubProductResponseDto;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Point;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.connection.stream.Consumer;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.ReadOffset;
import org.springframework.data.redis.connection.stream.RecordId;
import org.springframework.data.redis.connection.stream.StreamOffset;
import org.springframework.data.redis.connection.stream.StreamReadOptions;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.transaction.support.TransactionTemplate;

@Service
@RequiredArgsConstructor
@Slf4j
public class HubProductUseCase {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final String REDISSON_LOCK_PREFIX = "LOCK:CONFIRM_ORDER:";
    private static final int MAX_RETRY = 3;

    private final HubRepository hubRepository;
    private final ProductClient productClient;
    private final OrderClient orderClient;
    private final PassportHelper passportHelper;
    private final HubProductRepository hubProductRepository;
    private final RedissonClient redissonClient; // 분산 락 적용
    private final TransactionTemplate transactionTemplate; // 트랜잭션 경계 설정

    public HubProduct create(
            HubProductCreateRequestDto requestDto,
            String passport
    ) {
        log.info("producerId= {} ", requestDto.getProducerId());
        PassportDto passportDto = passportHelper.getPassportDto(passport);

        Hub hub = HubHelper.findByHubId(requestDto.getHubId(), hubRepository);

        HubProduct hubProduct = requestDto.toEntityWithHub(hub, requestDto);
        hubProduct.createdEntity(passportDto.getUserId());
        hubProductRepository.save(hubProduct);
        hub.getHubProduct()
                .add(hubProduct);

        hubRepository.save(hub);
        return hubProduct;
    }

    @Transactional
    public void delete(
            UUID hubProductId,
            String passport
    ) {
        PassportDto passportDto = passportHelper.getPassportDto(passport);
        HubProduct hubProduct = hubRepository.findByHubProductId(hubProductId)
                .orElseThrow(HubProductNotFoundException::new);

        hubProduct.deleteEntity(passportDto.getUserId());

        LocalDateTime deletedAt = hubProduct.getDeletedAt();
    }

    public HubProductOrderConfirmResponseDto confirmOrder(
            OrderConfirmRequestDto requestDto
    ) {
        /* TODO:
            ✅ 1. 상품 UUID로 검색 -> (hub.hubId, hub.Location, hub.huProduct.hubProductId, hub.huProduct.productName, hub.hubProduct.productType)
            ✅ 2. 검색된 상품들 hubId기준 으로 Map<UUID, List<hubProductDto>>로 그룹화
            ✅ 3. 우선순위 큐로 거리 기준으로 넣어버림. Comparator -> (o1, o2) -> o1.getDistance(o2)
            ✅ 4. 일단 조회된 상품 리스트 사이즈 == 넘겨받은 상품 사이즈랑 같은지 비교
            ✅ 5. 다르면 넘기고 같은 경우에만 실제 재고 비교.
            ✅ 6. 충분한 재고 확보하고 있으면 실제 재고 감소 시킴.
            ✅ 7. DTO 패킹해서 반환
         */
        List<Product> orderProducts = requestDto.getOrderProducts();
        // 상품 아이디 추출(상품 아이디 작은 순)
        List<UUID> productIds = orderProducts.stream()
                .map(Product::getProductId)
                .sorted()
                .toList();

        // 상품ID로 락 획득
        List<RLock> productLocks = productIds.stream()
                .map(id -> {
                    return redissonClient.getLock(REDISSON_LOCK_PREFIX + id);
                })
                .toList();

        String threadName = Thread.currentThread().getName();
        // multiLock 획득
        RLock orderLock = redissonClient.getMultiLock(productLocks.toArray(new RLock[0]));
        boolean available = false;
        log.info("{}, multiLock 획득 시도", threadName);
        HubProductOrderConfirmResponseDto confirmResponseDto = null;

        try {
            // 최대 3번까지 실행
            for (int attempts = 0; attempts < MAX_RETRY; attempts++) {
                available = orderLock.tryLock(9L, 6L, TimeUnit.SECONDS);
                if (available) {
                    break;
                }
                // 지수 백오프 적용
                long expBackOffMillis = (long) Math.pow(2, attempts) * 100
                        + ThreadLocalRandom.current().nextInt(100);
                Thread.sleep(expBackOffMillis);
            }

            // 락 획득 실패 시 예외
            if (!available) {
                // TODO: fallback 전략 추가 => RedisStream
                log.info("{} - fallback 발생!", threadName);
                throw new HubProductNotFoundException.LockAcquisitionFailedException();

            }

            log.info("{}, 락 획득 성공", threadName);
            confirmResponseDto = (HubProductOrderConfirmResponseDto) transactionTemplate.execute(
                    new TransactionCallback() {
                        @Override
                        public Object doInTransaction(TransactionStatus status) {
                            try {
                                return confirmOrderWithResponse(
                                        requestDto, orderProducts, productIds);
                            } catch (Exception e) {
                                status.setRollbackOnly();
                                return null;
                            }
                        }
                    });

        } catch (InterruptedException e) {
            throw new LockAcquisitionFailedException();
        } finally {
            log.info("작업 수행 완료");
            // 락 가지고 있을 때만 방출
            if (available) {
                orderLock.unlock();
            }
            log.info("{} 모든 락 해제 완료", threadName);
        }
        return confirmResponseDto;
    }

    protected HubProductOrderConfirmResponseDto confirmOrderWithResponse(
            OrderConfirmRequestDto
                    requestDto, List<Product> orderProducts, List<UUID> productIds) {
        Point consumerLocation = GeoUtil.createPoint(
                requestDto.getLatitude(),
                requestDto.getLongitude()
        );

        // 상품 재고 파악용
        Map<UUID, Product> productByProductId = new HashMap<>();

        orderProducts.forEach(
                product -> productByProductId.put(product.getProductId(), product)
        );

        log.info("모든 허브에서 상품들 조회");
        Map<UUID, List<HubProductInfoResponseDto>> hubProductSetByHub =
                findHubProductsGroupByHubId(productIds);

        // 수령업체에서 가장 가까운 허브부터 검색
        PriorityQueue<List<HubProductInfoResponseDto>> hubProductSetByDistance =
                new PriorityQueue<>(Comparator.comparing(
                        // 각 허브마다 최소 한 개의 원소는 보장됨
                        hp -> hp.get(0).getLocation().distance(consumerLocation)
                ));

        // 직선 거리 최단 순 우선순위 큐
        hubProductSetByDistance.addAll(hubProductSetByHub.values());

        while (!hubProductSetByDistance.isEmpty()) {
            List<HubProductInfoResponseDto> currentHub = hubProductSetByDistance.poll();

            log.info("상품 항목 개수 일치 확인 작업 수행");
            // 상품 개수가 맞지 않으면 볼 필요 X
            if (currentHub.size() != orderProducts.size()) {
                continue;
            }

            List<HubProductInfoResponseDto> toBeApply = new ArrayList<>();

            // O(N)
            for (HubProductInfoResponseDto hubProductInfo : currentHub) {

                Product orderProduct = productByProductId.getOrDefault(
                        hubProductInfo.getProductId(),
                        null
                );

                // 상품 ID가 다르거나 재고가 안맞으면 넘어감
                if (
                        orderProduct == null
                                || orderProduct.getQuantity()
                                > hubProductInfo.getProductStock()) {
                    continue;
                }
                // 재고 미리 감소 시켜봄. -> 엔티티가 아니므로 저장 시점이전까지 반영 X
                toBeApply.add(hubProductInfo); // 반영시킬 DTO 일단 넣어봄.
            }

            // 감소시킬 재고가 모두 있으면
            if (toBeApply.size() == orderProducts.size()) {

                return reduceStock(
                        toBeApply,
                        productByProductId
                );
            }

        }

        throw new HubProductException.HubProductStockNotEnoughException();
    }

    protected Map<UUID, List<HubProductInfoResponseDto>> findHubProductsGroupByHubId(
            List<UUID> productIds
    ) {
        // 요청 상품들과 일치하는 허브 상품 조회
        // 허브ID 기준으로 그룹화
        return hubRepository.findAllHubProductsInOrders(
                        productIds)
                .stream()
                // 허브ID 기준으로 그룹화
                .collect(Collectors.groupingBy(
                                HubProductInfoResponseDto::getHubId,
                                Collectors.toList()
                        )
                );
    }

    // 명시적으로 트랜잭션 종료 시켜 변경 내역 커밋
    protected HubProductOrderConfirmResponseDto reduceStock(
            List<HubProductInfoResponseDto> toBeApply,
            Map<UUID, Product> productByProductId
    ) {

        // 트랜잭션 추적
        TransactionSynchronizationManager.registerSynchronization(
                new TransactionSynchronization() {
                    @Override
                    public void afterCommit() {
                        log.info("{} 트랜잭션 커밋 완료", Thread.currentThread().getName());
                    }
                });

        List<UUID> hubProductIds =
                toBeApply.stream()
                        .map(HubProductInfoResponseDto::getHubProductId)
                        .toList();

        /*
        허브에서 아이디 바탕으로 조회(영속성 컨텍스트에 저장) 후 변경 감지 기능 사용
         */
        List<HubProduct> hubProducts = hubProductRepository.findAllHubProductsIn(
                hubProductIds);

        // 반환할 DTO 객체 준비
        HubProductOrderConfirmResponseDto confirmResponseDto =
                HubProductOrderConfirmResponseDto.builder()
                        .hubId(toBeApply.get(0).getHubId())
                        .hubName(toBeApply.get(0).getHubName())
                        .products(new ArrayList<>())
                        .build();

        for (HubProduct hubProduct : hubProducts) {
            // 상품 ID로 요청 수량 찾아오기
            Product product =
                    productByProductId.getOrDefault(hubProduct.getProductId(), null);

            if (product == null) {
                continue;
            }
            // 요청 수량만큼 재고 감소시킴
            hubProduct.reduceStock(product.getQuantity());

            // 반환 DTO에 넣기
            ConfirmedHubProductResponseDto confirmed = ConfirmedHubProductResponseDto
                    .builder()
                    .hubProductId(hubProduct.getHubProductId())
                    .productName(hubProduct.getProductName())
                    .productType(hubProduct.getProductType())
                    .quantity(Long.valueOf(product.getQuantity()))
                    .build();

            confirmResponseDto.addConfirmedProduct(confirmed);
        }
        return confirmResponseDto;
    }
}