package com.owlexpress.order.presentation.dto.request;

import com.owlexpress.order.domain.constant.OrderStatus;
import com.owlexpress.order.domain.constant.OrderType;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
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
}