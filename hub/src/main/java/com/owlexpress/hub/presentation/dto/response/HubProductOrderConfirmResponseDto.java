package com.owlexpress.hub.presentation.dto.response;

import com.owlexpress.hub.common.constant.ProductType;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HubProductOrderConfirmResponseDto {

    @Getter
    private UUID hubId;
    private List<ConfirmedHubProductResponseDto> products;

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class ConfirmedHubProductResponseDto {

        private UUID hubProductId;
        private String productName;
        private ProductType productType;
        private Long quantity;

        @Builder
        public ConfirmedHubProductResponseDto(UUID hubProductId, String productName,
                ProductType productType, Long quantity) {
            this.hubProductId = hubProductId;
            this.productName = productName;
            this.productType = productType;
            this.quantity = quantity;
        }
    }

    @Builder
    public HubProductOrderConfirmResponseDto(UUID hubId,
            List<ConfirmedHubProductResponseDto> products) {
        this.hubId = hubId;
        this.products = products;
    }

    public void addConfirmedProduct(ConfirmedHubProductResponseDto confirmed) {
        this.products.add(confirmed);
    }

    public List<ConfirmedHubProductResponseDto> getProducts() {
        return Collections.unmodifiableList(products);
    }
}
