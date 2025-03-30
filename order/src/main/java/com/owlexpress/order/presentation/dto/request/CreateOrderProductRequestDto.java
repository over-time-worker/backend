package com.owlexpress.order.presentation.dto.request;

import com.owlexpress.order.common.constant.ProductType;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrderProductRequestDto {

    private UUID productId;
    private Integer quantity;
    private String productName;
    private ProductType productType;
    private BigDecimal price;
}