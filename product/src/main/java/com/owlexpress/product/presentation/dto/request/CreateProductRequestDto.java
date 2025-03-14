package com.owlexpress.product.presentation.dto.request;

import com.owlexpress.product.domain.constant.ProductType;
import jakarta.validation.constraints.Size;
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
    @Size(min = 1, max = 50, message = "[size:productName]")
    private String productName;
    private ProductType productType;
    private BigDecimal productPrice;

}
