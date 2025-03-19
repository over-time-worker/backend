package com.owlexpress.payment.application.dto.response;

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
    private List<Product> products;

    @Builder
    public OrderFindResponseDto(BigDecimal totalPrice, List<Product> products) {
        this.totalPrice = totalPrice;
        this.products = products;
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Product {

        private UUID productId;
        private String productName;
        private Long amount;
        private BigDecimal price;

        public Product(UUID productId, String productName, Long amount, BigDecimal price) {
            this.productId = productId;
            this.productName = productName;
            this.amount = amount;
            this.price = price;
        }
    }

    public PaymentFindResponseDto toPaymentFindResponseDto() {
        return PaymentFindResponseDto.builder()
                .totalPrice(this.totalPrice)
                .productList(
                        products.stream()
                                .map(product ->
                                        PaymentFindResponseDto.Order.builder()
                                                .productId(product.getProductId())
                                                .productName(product.getProductName())
                                                .price(product.getPrice())
                                                .quantity(product.getAmount())
                                                .build()
                                ).toList()
                ).build();
    }
}
