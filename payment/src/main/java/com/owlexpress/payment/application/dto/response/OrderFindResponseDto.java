package com.owlexpress.payment.application.dto.response;

import com.owlexpress.payment.application.constant.ProductType;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderFindResponseDto {

    private BigDecimal totalPrice;
    private List<Product> orderProducts;

    @Builder
    public OrderFindResponseDto(BigDecimal totalPrice, List<Product> orderProducts) {
        this.totalPrice = totalPrice;
        this.orderProducts = orderProducts;
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Product {

        private UUID productId;
        private String productName;
        private BigDecimal price;
        private Long quantity;
        private ProductType productType;

        public Product(
                UUID productId,
                String productName,
                BigDecimal price,
                Long quantity,
                ProductType productType
        ) {
            this.productId = productId;
            this.productName = productName;
            this.price = price;
            this.quantity = quantity;
            this.productType = productType;
        }
    }

    public PaymentFindResponseDto toPaymentFindResponseDto() {
        return PaymentFindResponseDto.builder()
                .totalPrice(this.totalPrice)
                .orderProducts(
                        orderProducts.stream()
                                .map(product ->
                                        PaymentFindResponseDto.Order.builder()
                                                .productId(product.getProductId())
                                                .productName(product.getProductName())
                                                .productType(product.getProductType())
                                                .price(product.getPrice())
                                                .quantity(product.getQuantity())
                                                .build()
                                ).toList()
                ).build();
    }
}
