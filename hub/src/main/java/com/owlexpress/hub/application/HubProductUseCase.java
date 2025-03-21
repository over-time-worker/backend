package com.owlexpress.hub.application;

import com.owlexpress.hub.application.dto.response.HubProductInfoResponseDto;
import com.owlexpress.hub.common.dto.response.PassportDto;
import com.owlexpress.hub.common.exception.HubProductException.HubProductNotFoundException;
import com.owlexpress.hub.common.helper.HubHelper;
import com.owlexpress.hub.common.helper.PassportHelper;
import com.owlexpress.hub.domain.entity.Hub;
import com.owlexpress.hub.domain.entity.HubProduct;
import com.owlexpress.hub.domain.repository.HubProductRepository;
import com.owlexpress.hub.domain.repository.HubRepository;
import com.owlexpress.hub.infrastructure.client.OrderClient;
import com.owlexpress.hub.infrastructure.client.ProductClient;
import com.owlexpress.hub.presentation.dto.request.HubProductCreateRequestDto;
import com.owlexpress.hub.presentation.dto.request.OrderConfirmRequestDto;
import com.owlexpress.hub.presentation.dto.request.OrderConfirmRequestDto.Product;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class HubProductUseCase {

    private final HubRepository hubRepository;
    private final ProductClient productClient;
    private final OrderClient orderClient;
    private final PassportHelper passportHelper;
    private final HubProductRepository hubProductRepository;

    public HubProduct create(HubProductCreateRequestDto requestDto,
                             String passport
    ) {
        log.info("producerId= {} " , requestDto.getProducerId());
        PassportDto passportDto = passportHelper.getPassportDto(passport);

        Hub hub = HubHelper.findByHubId(requestDto.getHubId(), hubRepository);

        HubProduct hubProduct = requestDto.toEntityWithHub(hub,requestDto);
        hubProduct.createdEntity(passportDto.getUserId());
        hubProductRepository.save(hubProduct);
        hub.getHubProduct()
           .add(hubProduct);

        hubRepository.save(hub);
        return hubProduct;
    }

    @Transactional
    public void delete(UUID hubProductId,
                       String passport
    ) {
        PassportDto passportDto = passportHelper.getPassportDto(passport);
        HubProduct hubProduct = hubRepository.findByHubProductId(hubProductId)
                                             .orElseThrow(HubProductNotFoundException::new);

        hubProduct.deleteEntity(passportDto.getUserId());

        LocalDateTime deletedAt = hubProduct.getDeletedAt();

        /* TODO: 한 페이지가 끝이 아니라면, 전체 사이즈 / 사이즈(50)으로 반복 요청..?
            1. 변경 이후 시점의 모든 주문 검색
            PagedModel<OrderSearchResponseDto> data = orderClient.orderSearch(deletedAt, 1).getData();
            List<OrderSearchResponseDto> content = data.getContent();
         */

        /* TODO: <2 ~ 3>. 해당 주문들의 주문 내역에서 변경 상품이 있는지 확인
            List<UUID> cancelOrders = content.stream()
                .flatMap(order -> order.getOrderProducts().stream())
                .filter(orderProduct -> orderProduct.getProductId().equals(hubProductId))
                .map(OrderProduct::getOrderId)
                .forEach(orderId -> orderClient.deleteOrder(orderId));
         */

    }

    public void confirmOrder(OrderConfirmRequestDto requestDto) {
        /* TODO:
            1. 상품 UUID로 검색 -> (hub.hubId, hub.Location, hub.huProduct.hubProductId, hub.huProduct.productName, hub.hubProduct.productType)
            2. 검색된 상품들 hubId기준 으로 Map<UUID, List<hubProductDto>>로 그루핑
            3. 우선순위 큐로 거리 기준으로 넣어버림. Comparator -> (o1, o2) -> o1.getDistance(o2)
            4. 일단 조회된 상품 리스트 사이즈 == 넘겨받은 상품 사이즈랑 같은지 비교
            5. 다르면 넘기고 같은 경우에만 실제 재고 비교.
            6. 충분한 재고 확보하고 있으면 실제 재고 감소 시킴.
            7. DTO 패킹해서 반환
         */

        Point consumerLocation = requestDto.getLocation();
        List<Product> orderProducts = requestDto.getOrderProducts();

        Map<UUID, Product> productByProductId = new HashMap<>();

        orderProducts.forEach(
                product -> productByProductId.put(product.getProductId(), product)
        );

        // 상품 아이디 추출

        List<UUID> productIds = orderProducts.stream()
                .map(Product::getProductId)
                .toList();

        Map<UUID, List<HubProductInfoResponseDto>> hubProductSetByHub =
                // 상품UUID로 검색해서 같은 허브끼리 묶음.
                hubRepository.findAllHubProductsInOrders(
                                productIds)
                        .stream()
                        .collect(Collectors.groupingBy(
                                HubProductInfoResponseDto::getHubId,
                                Collectors.toList())
                        );

        // 수령업체에서 가장 가까운 허브부터 검색
        PriorityQueue<List<HubProductInfoResponseDto>> hubProductSetByDistance =
                new PriorityQueue<>(Comparator.comparing(
                        // 각 허브마다 최소 한 개의 원소는 보장됨
                        hp -> hp.get(0).getLocation().distance(consumerLocation)
                ));

        hubProductSetByDistance.addAll(hubProductSetByHub.values());

        while (!hubProductSetByDistance.isEmpty()) {
            List<HubProductInfoResponseDto> currentHub = hubProductSetByDistance.poll();

            // 상품 개수가 맞지 않으면 볼 필요 X
            if (currentHub.size() != orderProducts.size()) {
                continue;
            }

            List<HubProductInfoResponseDto> toBeReduced = new ArrayList<>();

            // O(N)
            for (HubProductInfoResponseDto hubProductInfo : currentHub) {

                Product orderProduct = productByProductId.getOrDefault(
                        hubProductInfo.getProductId(),
                        null
                );

                // 상품 ID가 다르거나 재고가 안맞으면 넘어감
                if (
                        orderProduct == null
                                || orderProduct.getQuantity() > hubProductInfo.getProductStock()) {
                    continue;
                }

                // 트랜잭션 분리 고려 (Propatgion.REQUIRES_NEW) -> 실제 재고 감소 로직은 여기서 처리?

                toBeReduced.add(hubProductInfo); // 감소시킬 재고도 기록해줘야 함.
                break;
            }

            // 감소시킬 재고가 모두 있으면
            if (toBeReduced.size() == orderProducts.size()) {
                for (HubProductInfoResponseDto hubProduct : toBeReduced) {
                    // TODO: HubProduct 엔티티로 변환 후 재고 감소 로직 실행.
                }

                /*
                TODO: HubProduct 엔티티 saveAll()로 저장. 왜? 현재는 DTO 프로젝션으로 조회해왔기 떄문에 영속성 컨텍스트에 엔티티 X
                    그렇기 때문에 저장을 직접 수행해줘야 함.
                 */

            }

        }


    }
}
