package com.owlexpress.order.presentation.dto.response;

import com.owlexpress.order.common.constant.OrderStatus;
import com.owlexpress.order.common.constant.OrderType;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderSearchResponseDto {
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
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    @Builder
    public OrderSearchResponseDto(
            UUID orderId,
            Long userId,
            UUID consumerId,
            UUID hubId,
            String consumerAddress,
            UUID deliveryId,
            BigDecimal totalPrice,
            String description,
            LocalDateTime requestArrivalTime,
            OrderType orderType,
            OrderStatus orderStatus,
            String productInfo,
            List<GetOrderProductResponseDto> orderProducts,
            LocalDateTime createdAt,
            LocalDateTime modifiedAt
    ) {
        this.orderId = orderId;
        this.userId = userId;
        this.consumerId = consumerId;
        this.hubId = hubId;
        this.consumerAddress = consumerAddress;
        this.deliveryId = deliveryId;
        this.totalPrice = totalPrice;
        this.description = description;
        this.requestArrivalTime = requestArrivalTime;
        this.orderType = orderType;
        this.orderStatus = orderStatus;
        this.productInfo = productInfo;
        this.orderProducts = orderProducts;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }
}
