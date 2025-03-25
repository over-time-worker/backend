package com.owlexpress.cart.common.dto.response;

import com.owlexpress.cart.domain.entity.constant.ProductType;
import com.owlexpress.cart.domain.entity.Cart;
import com.owlexpress.cart.domain.entity.CartProduct;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CartResponseDto {
    private UUID cartId;
    private BigDecimal totalPrice;

    private List<CartProductResponseDto> cartProduct = new ArrayList<>();

    @Builder
    public CartResponseDto (Cart cart, List<CartProduct> cartProducts) {
        this.cartId = cart.getCartId();
        this.totalPrice = cart.getTotalPrice();
        this.cartProduct = cartProducts.stream()
                .map(cp -> CartProductResponseDto.builder()
                        .cartProductId(cp.getCartProductId())
                        .productId(cp.getProductId())
                        .productName(cp.getProductName())
                        .productQuantity(cp.getProductQuantity())
                        .productPrice(cp.getProductPrice())
                        .productType(cp.getProductType())
                        .isSoldOut(cp.getIsSoldOut())
                        .build())
                .collect(Collectors.toList());
    }

    @Data
    @Builder
    public static class CartProductResponseDto {
        private UUID cartProductId;
        private UUID productId;
        private String productName;
        private Integer productQuantity;
        private BigDecimal productPrice;
        private ProductType productType;
        private Boolean isSoldOut;
    }
}
