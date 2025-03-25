package com.owlexpress.cart.common.dto.request;

import com.owlexpress.cart.domain.entity.constant.ProductType;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AddCartProductRequestDto {
    private UUID productId;
    private String productName;
    private BigDecimal productPrice;
    private Integer productQuantity;
    private ProductType productType;
}
