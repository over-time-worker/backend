package com.owlexpress.product.presentation.dto.response;

import com.owlexpress.product.domain.constant.ProductType;
import com.owlexpress.product.domain.entity.Product;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class FindProductResponse {
    private UUID productId;
    private String productName;
    private BigDecimal productPrice;
    private ProductType productType;

    @Builder
    public FindProductResponse(
            UUID productId,
            String productName,
            BigDecimal productPrice,
            ProductType productType
    )
    {
        this.productId = productId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productType = productType;
    }

    public static FindProductResponse toDTO(Product product) {
        return com.owlexpress.product.presentation.dto.response.FindProductResponse.builder()
                .productName(product.getProductName())
                .productId(product.getProductId())
                .productType(product.getProductType())
                .productPrice(product.getProductPrice())
                .build();
    }
}
