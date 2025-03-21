package com.owlexpress.product.common.dto.response;

import com.owlexpress.product.domain.constant.ProductType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HubProductFindResponseDto {
    private UUID hubProductId;

    private UUID hubId;

    private UUID producerId;

    private String producerName;

    private String productName;

    private ProductType productType;

    private Long productStock;


    @Builder
    public HubProductFindResponseDto(UUID hubProductId, UUID hubId, UUID producerId,
                                     String producerName, String productName, ProductType productType, Long productStock) {
        this.hubProductId = hubProductId;
        this.hubId = hubId;
        this.producerId = producerId;
        this.producerName = producerName;
        this.productName = productName;
        this.productType = productType;
        this.productStock = productStock;
    }
}
