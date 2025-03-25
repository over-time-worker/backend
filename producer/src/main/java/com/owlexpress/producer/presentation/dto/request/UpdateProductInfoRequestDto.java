package com.owlexpress.producer.presentation.dto.request;

import com.owlexpress.producer.common.constant.ProductType;
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
}
