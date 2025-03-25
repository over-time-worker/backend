package com.owlexpress.cart.presentation;

import com.owlexpress.cart.common.dto.request.AddCartProductRequestDto;
import com.owlexpress.cart.common.dto.request.CartProductDeleteRequestDto;
import com.owlexpress.cart.common.dto.response.CartResponseDto;
import java.util.UUID;

public interface CartService {
    void create(UUID consumerId, AddCartProductRequestDto addCartProductRequestDto, String passport);

    void increase(UUID cartId, UUID cartProductId, String passport);

    void decrease(UUID cartId, UUID cartProductId, String passport);

    void deleteCartProduct(UUID cartId, UUID cartProductId, String passport);

    void deleteCart(UUID consumerId, String passport);

    CartResponseDto find(String passport, UUID consumerId);

    void deleteCartProductsFromOrder(UUID consumerId, String passport, CartProductDeleteRequestDto requestDto);
}
