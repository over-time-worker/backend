package com.owlexpress.product.presentation.dto.request;

import com.owlexpress.product.domain.constant.ProductType;
import lombok.*;

import java.math.BigDecimal;

@Builder
@Data
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UpdateProductDto {
    private String productName;
    private ProductType productType;
    private BigDecimal productPrice;

    public static UpdateProductDto createMockingData() {
        return UpdateProductDto.builder()
                .productName("test상품")
                .productPrice(new BigDecimal(12000))
                .productType(ProductType.NORMAL)
                .build();
    }
}
