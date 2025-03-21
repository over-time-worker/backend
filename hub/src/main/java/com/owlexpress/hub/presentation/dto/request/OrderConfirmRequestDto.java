package com.owlexpress.hub.presentation.dto.request;

import java.util.List;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Point;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderConfirmRequestDto {

    private UUID consumerId;
    private Point location;
    private List<Product> orderProducts;

    public OrderConfirmRequestDto(UUID consumerId, Point location, List<Product> orderProducts) {
        this.consumerId = consumerId;
        this.location = location;
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
