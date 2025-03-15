package com.owlexpress.product.presentation.dto.response;

import com.owlexpress.product.domain.constant.ProductType;
import com.owlexpress.product.domain.entity.Product;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SearchProductResponseDto {
    private UUID productId;
    private String productName;
    private BigDecimal productPrice;
    private ProductType productType;
    private int totalQuantity;

    @Builder
    public SearchProductResponseDto(
            UUID productId,
            String productName,
            BigDecimal productPrice,
            ProductType productType,
            int totalQuantity
    )
    {
        this.productId = productId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productType = productType;
        this.totalQuantity = totalQuantity;
    }

    public static SearchProductResponseDto toDTO(Product product, AtomicInteger totalQuantity) {
        return SearchProductResponseDto.builder()
                .productName(product.getProductName())
                .productId(product.getProductId())
                .productType(product.getProductType())
                .productPrice(product.getProductPrice())
                .totalQuantity(totalQuantity.get())
                .build();
    }

}
