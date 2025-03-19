package com.owlexpress.hub.presentation.dto.request;

import com.owlexpress.hub.common.ProductType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HubProductUpdateRequestDto {

    private UUID hubProductId;
    private String hubProductName;
    private ProductType hubProductType;
    private Long hubProductStock;

    @Builder
    public HubProductUpdateRequestDto(UUID hubProductId, String hubProductName,
            ProductType hubProductType, Long hubProductStock) {
        this.hubProductId = hubProductId;
        this.hubProductName = hubProductName;
        this.hubProductType = hubProductType;
        this.hubProductStock = hubProductStock;
    }
}
