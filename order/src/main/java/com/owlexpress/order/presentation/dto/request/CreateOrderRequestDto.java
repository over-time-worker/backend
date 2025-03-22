package com.owlexpress.order.presentation.dto.request;

import com.owlexpress.order.domain.constant.OrderStatus;
import com.owlexpress.order.domain.constant.OrderType;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreateOrderRequestDto {
    private UUID consumerId;
    private UUID hubId;
    private String consumerAddress;
    private UUID deliveryId;
    private String description;
    private LocalDateTime requestArrivalTime;
    private OrderType orderType;
    private OrderStatus orderStatus;
    private String productInfo;
    private List<CreateOrderProductRequestDto> products;

    @Builder
    public CreateOrderRequestDto (
            UUID consumerId,
            UUID hubId,
            String consumerAddress,
            UUID deliveryId,
            String description,
            LocalDateTime requestArrivalTime,
            OrderType orderType,
            OrderStatus orderStatus,
            String productInfo,
            List<CreateOrderProductRequestDto> products
    ){
        this.consumerId = consumerId;
        this.hubId = hubId;
        this.consumerAddress = consumerAddress;
        this.deliveryId = deliveryId;
        this.description = description;
        this.requestArrivalTime = requestArrivalTime;
        this.orderStatus = orderStatus;
        this.orderType = orderType;
        this.productInfo = productInfo;
        this.products = products;
    }
}