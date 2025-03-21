package com.owlexpress.product.common.dto.request;

import com.owlexpress.product.common.dto.response.HubProductFindResponseDto;
import com.owlexpress.product.domain.constant.ProductType;
import com.owlexpress.product.presentation.dto.request.UpdateProductDto;
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

    public static HubProductUpdateRequestDto toDto(
            HubProductFindResponseDto hubProductFindResponseDto,
            UpdateProductDto updateProductDto
    ) {
        return HubProductUpdateRequestDto.builder()
                                  .hubProductId(hubProductFindResponseDto.getHubProductId())
                                  .hubProductName(updateProductDto.getProductName())
                                  .hubProductType(updateProductDto.getProductType())
                                  .hubProductStock(hubProductFindResponseDto.getProductStock())
                                  .build();
    }
}
