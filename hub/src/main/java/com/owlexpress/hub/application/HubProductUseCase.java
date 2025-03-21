package com.owlexpress.hub.application;

import com.owlexpress.hub.application.dto.response.HubProductInfoResponseDto;
import com.owlexpress.hub.common.dto.response.PassportDto;
import com.owlexpress.hub.common.helper.HubHelper;
import com.owlexpress.hub.common.exception.HubProductException.HubProductNotFoundException;
import com.owlexpress.hub.common.helper.PassportHelper;
import com.owlexpress.hub.domain.entity.Hub;
import com.owlexpress.hub.domain.entity.HubProduct;
import com.owlexpress.hub.domain.repository.HubRepository;
import com.owlexpress.hub.infrastructure.client.OrderClient;
import com.owlexpress.hub.infrastructure.client.ProductClient;
import com.owlexpress.hub.presentation.dto.request.HubProductCreateRequestDto;
import com.owlexpress.hub.presentation.dto.request.OrderConfirmRequestDto;
import com.owlexpress.hub.presentation.dto.request.OrderConfirmRequestDto.Product;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class HubProductUseCase {

    private final HubRepository hubRepository;
    private final ProductClient productClient;
    private final OrderClient orderClient;
    private final PassportHelper passportHelper;

    public HubProduct create(HubProductCreateRequestDto requestDto,
            String passport
    ) {
        PassportDto passportDto = passportHelper.getPassportDto(passport);

        Hub hub = HubHelper.findByHubId(requestDto.getHubId(), hubRepository);

        // TODO: 상품 있는지 체크

        HubProduct hubProduct = requestDto.toEntityWithHub(hub);
        hubProduct.createdEntity(passportDto.getUserId());
        hub.getHubProduct()
                .add(hubProduct);
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
        // 상품 아이디 추출
        List<UUID> productIds = requestDto.getOrderProducts().stream()
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
        PriorityQueue<HubProductInfoResponseDto> hubProductSetByDistance =
                new PriorityQueue<>(Comparator.comparing(
                        hp -> hp.getLocation().distance(consumerLocation)
                ));




    }
}
