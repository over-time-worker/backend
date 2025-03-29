package com.owlexpress.hub.presentation.dto.request;

import java.util.List;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class
OrderConfirmRequestDto {

    private UUID orderId;
    private UUID consumerId;
    private Double latitude;
    private Double longitude;
    private List<Product> orderProducts;

    public OrderConfirmRequestDto(
            UUID orderId,
            UUID consumerId,
            Double latitude,
            Double longitude,
            List<Product> orderProducts
    ) {
        this.orderId = orderId;
        this.consumerId = consumerId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.orderProducts = orderProducts;
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Product {

        private UUID productId;
        private Integer quantity;

        public Product(UUID productId, Integer quantity) {
            this.productId = productId;
            this.quantity = quantity;
        }
    }
}
