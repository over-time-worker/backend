package com.owlexpress.product.presentation.dto.request;

import com.owlexpress.product.domain.constant.ProductType;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;

@Builder
@Data
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UpdateProductDto {
    @Size(min = 1, max = 50, message = "[size:productName]")
    private String productName;
    private ProductType productType;
    private BigDecimal productPrice;

}
