package com.owlexpress.order.presentation.dto.response;

import com.owlexpress.order.domain.constant.ProductType;
import com.owlexpress.order.domain.entity.OrderProduct;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class GetOrderProductResponseDto {
    private UUID orderProductId;
    private UUID orderId;
    private UUID productId;
    private Integer quantity;
    private String productName;
    private ProductType productType;
    private BigDecimal amount;
    private BigDecimal price;

    public static GetOrderProductResponseDto fromEntity(OrderProduct orderProduct) {
        return GetOrderProductResponseDto.builder()
                .orderProductId(orderProduct.getOrderProductId())
                .orderId(orderProduct.getOrderId())
                .productId(orderProduct.getProductId())
                .productName(orderProduct.getProductName())
                .quantity(orderProduct.getQuantity())
                .productType(orderProduct.getProductType())
                .amount(orderProduct.getAmount())
                .price(orderProduct.getPrice())
                .build();
    }
}