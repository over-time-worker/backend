package com.owlexpress.order.application.dto.response;

import com.owlexpress.order.domain.constant.ProductType;
import java.util.List;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ConfirmHubStockResponseDto {
    private UUID hubId;
    private String hubName;
    private List<ConFirmHubStockProductResponseDto> products;

    @Builder
    public ConfirmHubStockResponseDto(
            UUID hubId,
            String hubName,
            List<ConFirmHubStockProductResponseDto> products
    ) {
        this.hubId = hubId;
        this.hubName = hubName;
        this.products = products;
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class ConFirmHubStockProductResponseDto {
        private UUID hubProductId;
        private String productName;
        private ProductType productType;
        private Long quantity;

        @Builder
        public ConFirmHubStockProductResponseDto(UUID hubProductId, String productName,
                ProductType productType, Long quantity) {
            this.hubProductId = hubProductId;
            this.productName = productName;
            this.productType = productType;
            this.quantity = quantity;
        }
    }
}
