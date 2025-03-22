package com.owlexpress.order.application.dto.request;

import java.util.List;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Point;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ConfirmHubStockRequestDto {

    private UUID consumerId;
    private Point location;
    private List<HubProduct> orderProducts;

    @Builder
    public ConfirmHubStockRequestDto(UUID consumerId, Point location, List<HubProduct> orderProducts) {
        this.consumerId = consumerId;
        this.location = location;
        this.orderProducts = orderProducts;
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class HubProduct {

        private UUID productId;
        private Integer quantity;

        public HubProduct(UUID productId, Integer quantity) {
            this.productId = productId;
            this.quantity = quantity;
        }
    }
}
