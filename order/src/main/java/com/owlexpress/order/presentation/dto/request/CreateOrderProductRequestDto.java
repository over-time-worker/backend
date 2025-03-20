package com.owlexpress.order.presentation.dto.request;

import com.owlexpress.order.domain.constant.ProductType;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateOrderProductRequestDto {
    private UUID productId;
    private Integer quantity;
    private String productName;
    private ProductType productType;
    private BigDecimal price;
}