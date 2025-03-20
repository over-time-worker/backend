package com.owlexpress.hub.application;

import com.owlexpress.hub.common.HubHelper;
import com.owlexpress.hub.common.exception.HubProductException.HubProductNotFoundException;
import com.owlexpress.hub.domain.entity.Hub;
import com.owlexpress.hub.domain.entity.HubProduct;
import com.owlexpress.hub.domain.repository.HubRepository;
import com.owlexpress.hub.infrastructure.client.OrderClient;
import com.owlexpress.hub.infrastructure.client.ProductClient;
import com.owlexpress.hub.presentation.dto.request.HubProductCreateRequestDto;
import lombok.RequiredArgsConstructor;
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

    public HubProduct create(HubProductCreateRequestDto requestDto) {

        Hub hub = HubHelper.findByHubId(requestDto.getHubId(), hubRepository);

        // TODO: 상품 있는지 체크

        HubProduct hubProduct = requestDto.toEntityWithHub(hub);
        // TODO: PASSPORT에서 값 뺴오기
        hubProduct.createdEntity(1L);
        hub.getHubProduct().add(hubProduct);
        return hubProduct;
    }

    @Transactional
    public void delete(UUID hubProductId) {
        HubProduct hubProduct = hubRepository.findByHubProductId(hubProductId)
                .orElseThrow(HubProductNotFoundException::new);

        // TODO: PASSPORT 에서 값 뺴오기
        hubProduct.deleteEntity(1L);

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

}
