package com.owlexpress.order.presentation.dto.request;

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
    private String consumerAddress;
    private String description;
    private LocalDateTime requestArrivalTime;
    private OrderType orderType;
    private List<CreateOrderProductRequestDto> products;

    @Builder
    public CreateOrderRequestDto (
            UUID consumerId,
            String consumerAddress,
            String description,
            LocalDateTime requestArrivalTime,
            OrderType orderType,
            List<CreateOrderProductRequestDto> products
    ){
        this.consumerId = consumerId;
        this.consumerAddress = consumerAddress;
        this.description = description;
        this.requestArrivalTime = requestArrivalTime;
        this.orderType = orderType;
        this.products = products;
    }
}