package com.owlexpress.product.presentation.dto.request;

import com.owlexpress.product.domain.constant.ProductType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateProductRequestDto {
    private String productName;
    private ProductType productType;
    private BigDecimal productPrice;

}
