package com.owlexpress.order.application.dto.request;

import java.util.List;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ConfirmHubStockRequestDto {

    private UUID consumerId;
    private Double latitude;
    private Double longitude;
    private List<HubProduct> orderProducts;

    @Builder
    public ConfirmHubStockRequestDto(UUID consumerId, Double latitude, Double longitude, List<HubProduct> orderProducts) {
        this.consumerId = consumerId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.orderProducts = orderProducts;
    }

    @Builder
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
