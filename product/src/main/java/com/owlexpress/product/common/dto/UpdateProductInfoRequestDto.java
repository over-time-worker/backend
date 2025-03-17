package com.owlexpress.product.common.dto;

import com.owlexpress.product.domain.constant.ProductType;
import com.owlexpress.product.domain.entity.Product;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class UpdateProductInfoRequestDto {
    @NotNull(message = "상품명은 필수값입니다.")
    @Size(min = 1, max = 50)
    private String productName;
    @NotNull(message = "상품 가격은 필수값입니다.")
    private BigDecimal productPrice;
    @NotNull(message = "상품 타입은 필수값입니다(NORMAL, FRESH)")
    private ProductType productType;

    @Builder
    public UpdateProductInfoRequestDto(
            String productName,
            BigDecimal productPrice,
            ProductType productType
    ) {
        this.productName = productName;
        this.productPrice = productPrice;
        this.productType = productType;
    }

    public static UpdateProductInfoRequestDto fromEntity(Product product) {
        return UpdateProductInfoRequestDto.builder()
                                   .productName(product.getProductName())
                                   .productPrice(product.getProductPrice())
                                   .productType(product.getProductType())
                                   .build();
    }
}
