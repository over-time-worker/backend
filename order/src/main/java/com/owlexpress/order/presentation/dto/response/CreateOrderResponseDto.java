package com.owlexpress.order.presentation.dto.response;

import com.owlexpress.order.common.constant.OrderStatus;
import com.owlexpress.order.domain.entity.Order;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CreateOrderResponseDto {
    private UUID orderId;
    private BigDecimal totalPrice;
    private OrderStatus orderStatus;

    @Builder
    public CreateOrderResponseDto(
            UUID orderId,
            BigDecimal totalPrice,
            OrderStatus orderStatus
    ) {
        this.orderId = orderId;
        this.totalPrice = totalPrice;
        this.orderStatus = orderStatus;
    }

    public static CreateOrderResponseDto toDto(Order save) {

        return CreateOrderResponseDto.builder()
                .orderId(save.getOrderId())
                .totalPrice(save.getTotalPrice())
                .orderStatus(save.getOrderStatus())
                .build();
    }
}
