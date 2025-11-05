package com.owlexpress.hub.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.owlexpress.hub.application.dto.response.HubProductInfoPreProcessResponseDto;
import com.owlexpress.hub.common.dto.response.PassportDto;
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
import com.owlexpress.hub.presentation.HubProductUseCase;
import com.owlexpress.hub.presentation.dto.request.HubProductCreateRequestDto;
import com.owlexpress.hub.presentation.dto.request.OrderConfirmRequestDto;
import com.owlexpress.hub.presentation.dto.request.OrderConfirmRequestDto.Product;
import com.owlexpress.hub.presentation.dto.response.HubProductOrderConfirmResponseDto;
import com.owlexpress.hub.presentation.dto.response.HubProductOrderConfirmResponseDto.ConfirmedHubProductResponseDto;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Point;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.transaction.support.TransactionTemplate;

@Service
@RequiredArgsConstructor
@Slf4j
public class HubProductUseCaseImpl implements HubProductUseCase {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final String REDISSON_LOCK_PREFIX = "LOCK:HUB_PRODUCT:CONFIRM_ORDER:";
    private static final String REDUCED_PRODUCTS_CACHE_PREFIX = "HUB_PRODUCT:CONFIRM_ORDER:";
    private static final int MAX_RETRY = 3;

    private final HubRepository hubRepository;
    private final ProductClient productClient;
    private final OrderClient orderClient;
    private final PassportHelper passportHelper;
    private final HubProductRepository hubProductRepository;
    private final RedissonClient redissonClient; // 분산 락 적용
    private final TransactionTemplate transactionTemplate; // 트랜잭션 경계 설정
    private final RedisTemplate<String, Object> reducedProducts; // 주문 ID : [{허브 상품 ID : 개수}, ...]

    @Override
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
    @Override
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

    /**
     * 시스템 제약 : 주문한 모든 상품이 하나의 허브에 있는 경우에만 주문 가능하다.
     */


    @Override
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

        HubProductOrderConfirmResponseDto confirmResponseDto = null;

        confirmResponseDto = confirmOrderWithResponse(requestDto, orderProducts, productIds);
        // 재고 감소 성공 했다면 -> 롤백 대비해서 캐싱
        List<ConfirmedHubProductResponseDto> products = null;
        if (confirmResponseDto != null
            && (products = confirmResponseDto.getProducts()) != null) {
            log.info("{} - 재고 감소 성공, 캐싱 진행", Thread.currentThread().getName());
            cacheReducedProducts(requestDto.getOrderId(), products);
        }
        return confirmResponseDto;
    }

    public void rollbackConfirmOrder(UUID orderId) {
        String threadName = Thread.currentThread().getName();
        log.info("{} - 재고 롤백 시도", threadName);
        // 레디스에서 키값으로 롤백 준비
        List<ConfirmedHubProductResponseDto> reducedHubProducts = getReducedHubProducts(
            orderId);

        List<RLock> rollbackLocks = reducedHubProducts.stream()
            .map(dto -> redissonClient.getLock(REDISSON_LOCK_PREFIX + dto.getHubProductId()))
            .toList();

        RLock rollbackOrderLock = redissonClient.getMultiLock(
            rollbackLocks.toArray(new RLock[0]));

        boolean available = false;

        try {
            for (int attempts = 0; attempts < MAX_RETRY; attempts++) {
                // 락 획득 시도
                log.info("{} - 재고 롤백 락 획득 시도", threadName);
                available = rollbackOrderLock.tryLock(500L, 400L, TimeUnit.MILLISECONDS);

                if (available) {
                    break;
                }

                // 지수 백오프 적용
                long expBackOffMillis = (long) Math.pow(2, attempts + 1) * 100
                    + ThreadLocalRandom.current().nextInt(100);
                Thread.sleep(expBackOffMillis);
            }

            // 락 획득 실패 -> fallback
            if (!available) {
                log.error("{} - 재고 롤백 중 fallback 발생!", threadName);
                throw new HubProductNotFoundException.LockAcquisitionFailedException(
                    threadName);
            }

            log.info("{} - 재고 롤백 시도", threadName);
            List<UUID> hubProductIds = reducedHubProducts.stream()
                .map(ConfirmedHubProductResponseDto::getHubProductId)
                .toList();

            // 허브 상품 ID 별로 상품 개수를 가지는 맵
            Map<UUID, Long> byHubProductId = new HashMap<>();

            // 맵에 삽입
            reducedHubProducts.forEach(
                hubProduct -> byHubProductId.put(
                    hubProduct.getHubProductId(),
                    hubProduct.getQuantity()
                )
            );

            transactionTemplate.execute(new TransactionCallbackWithoutResult() {
                @Override
                protected void doInTransactionWithoutResult(TransactionStatus status) {
                    try {

                        List<HubProduct> hubProductStocks = hubRepository.findHubProductStocks(
                            hubProductIds);

                        for (HubProduct hubProduct : hubProductStocks) {
                            UUID hubProductId = hubProduct.getHubProductId();

                            Long quantity = byHubProductId.getOrDefault(hubProductId, null);

                            if (quantity == null) {
                                return;
                            }
                            hubProduct.addStock(quantity);
                        }

                    } catch (IllegalMonitorStateException e) {
                        log.error("{} - 재고 롤백 도중 실패", threadName);
                        status.setRollbackOnly();
                    }
                }
            });
            log.info("{} - 롤백 수행 완료", threadName);

        } catch (InterruptedException e) {
            throw new LockAcquisitionFailedException(threadName);
        } finally {
            // 락 가지고 있을 때만 방출
            try {
                rollbackOrderLock.unlock();
                log.info("{} - 재고 롤백 락 해제 완료", threadName);
            } catch (Exception e) {
                log.warn("{} - 재고 롤백, 락 미보유 혹은 이미 반환완료", threadName);
            }
        }

    }

    protected HubProductOrderConfirmResponseDto confirmOrderWithResponse(
        OrderConfirmRequestDto
            requestDto, List<Product> orderProducts, List<UUID> productIds) {
        HubProductOrderConfirmResponseDto res = null;
        long start_transaction = System.currentTimeMillis();
        long start = 0;
        Point consumerLocation = GeoUtil.createPoint(
            requestDto.getLatitude(),
            requestDto.getLongitude()
        );
        String threadName = Thread.currentThread().getName();

        // 상품 재고 파악용
        Map<UUID, Product> productByProductId = new HashMap<>();

        orderProducts.forEach(
            product -> productByProductId.put(product.getProductId(), product)
        );

        log.info("{} - 상품을 가진 허브 조회", threadName);
        Map<UUID, List<HubProductInfoPreProcessResponseDto>> hubProductSetByHub =
            findHubProductsGroupByHubId(productIds);

        // 수령업체에서 가장 가까운 허브부터 검색
        PriorityQueue<List<HubProductInfoPreProcessResponseDto>> hubProductSetByDistance =
            new PriorityQueue<>(Comparator.comparing(
                // 각 허브마다 최소 한 개의 원소는 보장됨
                hp -> hp.get(0).getLocation().distance(consumerLocation)
            ));

        // 직선 거리 최단 순 우선순위 큐
        hubProductSetByDistance.addAll(hubProductSetByHub.values());
        // 조회 메서드 변화로 모든 상품이 있는 경우에만 들어감. 따라서, 구현 사항도 변경 예정.
        while (!hubProductSetByDistance.isEmpty()) {
            List<HubProductInfoPreProcessResponseDto> currentHub = hubProductSetByDistance.poll();

            String currentHubName = currentHub.get(0).getHubName();
            UUID currentHubId = currentHub.get(0).getHubId();

            List<UUID> hubProductsInCurrentHub = currentHub.stream()
                .map(HubProductInfoPreProcessResponseDto::getHubProductId)
                .toList();

            // 상품이 충분한지 검증.
            // TODO: 락 시작
            // TODO: 트랜잭션 경계 시작
            RLock[] lockKeys = currentHub.stream()
                .map(HubProductInfoPreProcessResponseDto::getHubProductId)
                .sorted()
                .map(hubProductId -> REDISSON_LOCK_PREFIX + hubProductId)
                .map(redissonClient::getLock)
                .toArray(RLock[]::new);

            RLock orderLock = redissonClient.getMultiLock(lockKeys);
            boolean available = false;
            try {
                for (int attempts = 0; attempts < MAX_RETRY; attempts++) {
                    available = orderLock.tryLock(400L, 700L, TimeUnit.MILLISECONDS);

                    if (available) {
                        break;
                    }

                    long expBackOffMillis = (long) Math.pow(2, attempts + 1) * 100 +
                        ThreadLocalRandom.current().nextInt(100);
                    Thread.sleep(expBackOffMillis);
                }

                // 락 획득 실패 시 예외
                if (!available) {
                    // TODO: fallback 전략 추가 => RedisStream
                    log.error("{} - fallback 발생!", threadName);
                    throw new HubProductNotFoundException.LockAcquisitionFailedException(
                        threadName);
                }
                start = System.currentTimeMillis();
                log.info("{} - {}에서 상품 재고 조회", threadName, currentHubId);

                List<HubProduct> hubProductStocks = hubRepository.findHubProductStocks(
                    hubProductsInCurrentHub);

                Map<UUID, HubProduct> byProductId = hubProductStocks
                    .stream()
                    .collect(
                        Collectors.toUnmodifiableMap(HubProduct::getProductId,
                            Function.identity())
                    );

                boolean isAllProductEnough = true;

                for (OrderConfirmRequestDto.Product desiredProduct : requestDto.getOrderProducts()) {

                    HubProduct real = byProductId.get(desiredProduct.getProductId());

                    // 재고가 부족하면 탐색 X.
                    if (desiredProduct.getQuantity() > real.getProductStock()) {
                        isAllProductEnough = false;
                        break;
                    }

                }

                // 모든 재고가 충분치 않으면 다음 허브 탐색
                if (!isAllProductEnough) {
                    continue;
                }

                res = (HubProductOrderConfirmResponseDto) transactionTemplate.execute(
                    new TransactionCallback() {
                        @Override
                        public Object doInTransaction(TransactionStatus status) {
                            try {
                                return reduceStock(
                                    hubProductStocks,
                                    productByProductId,
                                    currentHubId,
                                    currentHubName,
                                    requestDto.getOrderId()
                                );
                            } catch (Exception e) {
                                log.error("{} - 재고 감소 중 문제 발생 : {}", threadName, e.getMessage());
                                status.setRollbackOnly();
                                return null;
                            }
                        }
                    });
                break;
                // 감소시킬 재고가 모두 있으면
            } catch (InterruptedException e) {
                throw new LockAcquisitionFailedException(threadName);
            } finally {
                // TODO: 트랜잭션 경계 종료
                // TODO: 락 해제
                try {
                    orderLock.unlock();
                    long end = System.currentTimeMillis();
                    log.info("락 보유 시간 : {}ms, 총 소요 시간 = {}", end - start, end - start_transaction);
                    log.info("{} - 락 해제 완료", threadName);
                } catch (Exception e) {
                    log.warn("{} - 락 미보유 혹은 이미 반환완료", threadName);
                }
            }
        }
        if (res != null) {
            return res;
        }
        throw new HubProductException.HubProductStockNotEnoughException();
    }

    protected Map<UUID, List<HubProductInfoPreProcessResponseDto>> findHubProductsGroupByHubId(
        List<UUID> productIds
    ) {
        // 요청 상품들과 일치하는 허브 상품 조회
        // 허브ID 기준으로 그룹화
        return hubRepository.findAllHubProductsInOrders(
                productIds)
            .stream()
            // 허브ID 기준으로 그룹화
            .collect(Collectors.groupingBy(
                    HubProductInfoPreProcessResponseDto::getHubId,
                    Collectors.toList()
                )
            );
    }

    // 명시적으로 트랜잭션 종료 시켜 변경 내역 커밋
    protected HubProductOrderConfirmResponseDto reduceStock(
        List<HubProduct> toBeApply,
        Map<UUID, Product> productByProductId,
        UUID hubId,
        String hubName,
        UUID orderId
    ) {

        // 트랜잭션 추적
        TransactionSynchronizationManager.registerSynchronization(
            new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    log.info("{} 트랜잭션 커밋 완료", Thread.currentThread().getName());
                }
            });

        // 반환할 DTO 객체 준비
        HubProductOrderConfirmResponseDto confirmResponseDto =
            HubProductOrderConfirmResponseDto.builder()
                .hubId(hubId)
                .hubName(hubName)
                .products(new ArrayList<>())
                .build();

        for (HubProduct hubProduct : toBeApply) {
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

    private void cacheReducedProducts(UUID
        orderId, List<ConfirmedHubProductResponseDto> products) {
        String cacheKey = REDUCED_PRODUCTS_CACHE_PREFIX + orderId;
        try {
            String values = objectMapper.writeValueAsString(products);
            reducedProducts.opsForValue().set(cacheKey, values);
            log.info("{} - 캐싱 성공", Thread.currentThread().getName());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private List<ConfirmedHubProductResponseDto> getReducedHubProducts(UUID orderId) {
        String cacheKey = REDUCED_PRODUCTS_CACHE_PREFIX + orderId;
        try {
            String json = (String) reducedProducts.opsForValue().get(cacheKey);
            if (json == null) {
                return Collections.emptyList();
            }
            JavaType type = objectMapper.getTypeFactory()
                .constructCollectionType(List.class, ConfirmedHubProductResponseDto.class);
            return objectMapper.readValue(json, type);
        } catch (Exception e) {
            throw new RuntimeException("Failed to deserialize list", e);
        }
    }

}