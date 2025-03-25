package com.owlexpress.order.presentation.dto.response;

import com.owlexpress.order.common.constant.OrderStatus;
import com.owlexpress.order.common.constant.OrderType;
import com.owlexpress.order.domain.entity.Order;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GetOrderResponseDto {
    private UUID orderId;
    private Long userId;
    private UUID consumerId;
    private UUID hubId;
    private String consumerAddress;
    private UUID deliveryId;
    private BigDecimal totalPrice;
    private String description;
    private LocalDateTime requestArrivalTime;
    private OrderType orderType;
    private OrderStatus orderStatus;
    private String productInfo;
    private List<GetOrderProductResponseDto> orderProducts;

    @Builder
    public GetOrderResponseDto(Order order) {
        this.orderId = order.getOrderId();
        this.userId = order.getUserId();
        this.consumerId = order.getConsumerId();
        this.hubId = order.getHubId();
        this.consumerAddress = order.getConsumerAddress();
        this.deliveryId = order.getDeliveryId();
        this.totalPrice = order.getTotalPrice();
        this.description = order.getDescription();
        this.requestArrivalTime = order.getRequestArrivalTime();
        this.orderType = order.getOrderType();
        this.orderStatus = order.getOrderStatus();
        this.productInfo = order.getProductInfo();
        this.orderProducts = order.getOrderProducts().stream()
                .map(GetOrderProductResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    public static GetOrderResponseDto toDto(Order order) {
        return GetOrderResponseDto.builder()
                .order(order)
                .build();
    }
}