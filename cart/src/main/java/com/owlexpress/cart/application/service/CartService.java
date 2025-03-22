package com.owlexpress.cart.application.service;

import com.owlexpress.cart.presentation.dto.request.AddCartProductRequestDto;
import com.owlexpress.cart.presentation.dto.request.CartProductDeleteRequestDto;
import com.owlexpress.cart.presentation.dto.response.CartResponseDto;
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
