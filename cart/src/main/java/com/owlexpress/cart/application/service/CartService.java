package com.owlexpress.cart.application.service;

import com.owlexpress.cart.presentation.dto.request.AddCartProductRequestDto;
import com.owlexpress.cart.presentation.dto.response.CartResponseDto;
import java.util.UUID;

public interface CartService {
    void create(UUID consumerId, AddCartProductRequestDto addCartProductRequestDto, Long userId);

    void increase(UUID cartId, UUID cartProductId, Long userId);

    void decrease(UUID cartId, UUID cartProductId, Long userId);

    void deleteCartProduct(UUID cartId, UUID cartProductId, Long userId);

    void deleteCart(UUID cartId, Long userId);

    CartResponseDto find(UUID consumerId);
}
