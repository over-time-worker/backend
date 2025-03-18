package com.owlexpress.cart.application.service;

import static com.owlexpress.cart.common.exception.ExceptionMessage.CART_NOT_FOUND_EXCEPTION_MESSAGE;

import com.owlexpress.cart.application.exception.CartNotFoundException;
import com.owlexpress.cart.domain.CartRepository;
import com.owlexpress.cart.domain.entity.Cart;
import com.owlexpress.cart.domain.entity.CartProduct;
import com.owlexpress.cart.presentation.dto.request.AddCartProductRequestDto;
import com.owlexpress.cart.presentation.dto.response.CartResponseDto;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;

    @Transactional
    @Override
    public void create(
            UUID consumerId,
            AddCartProductRequestDto addCartProductRequestDto,
            Long userId
    ) {
        Cart cart = findOrCreateCart(
                consumerId,
                userId,
                addCartProductRequestDto.getProductPrice()
        );

        CartProduct cartProduct = createCartProduct(cart, addCartProductRequestDto, userId);

        cart.addCartProduct(cartProduct);
        cart.updateModifiedData(userId);  // TODO: AuditAware 적용 후 삭제

        cartRepository.save(cart);
    }

    @Transactional
    @Override
    public void increase(UUID cartId, UUID cartProductId, Long userId) {
        Cart cart = findCartByCartId(cartId);
        cart.increaseCartProductQuantity(cartProductId, userId);
    }

    @Transactional
    @Override
    public void decrease(UUID cartId, UUID cartProductId, Long userId) {
        Cart cart = findCartByCartId(cartId);
        cart.decreaseCartProductQuantity(cartProductId, userId);
    }

    @Transactional
    @Override
    public void deleteCartProduct(UUID cartId, UUID cartProductId, Long userId) {
        Cart cart = findCartByCartId(cartId);
        cart.removeCartProduct(cartProductId, userId);
    }

    @Transactional
    @Override
    public void deleteCart(UUID cartId, Long userId) {
        Cart cart = findCartByCartId(cartId);
        cart.removeCart(userId);
    }

    @Override
    public CartResponseDto find(UUID consumerId) {
        Cart cart = findCartByConsumerId(consumerId);

        return CartResponseDto.builder()
                .cart(cart)
                .cartProducts(cart.getCartProductList())
                .build();
    }

    private Cart findOrCreateCart(UUID consumerId, Long userId, BigDecimal initialPrice) {
        return cartRepository.findByConsumerId(consumerId)
                .orElseGet(() -> {
                    Cart newCart = Cart.builder()
                            .consumerId(consumerId)
                            .totalPrice(initialPrice)
                            .build();
                    newCart.updateCreateData(userId);  // TODO: AuditAware 적용 후 삭제
                    return cartRepository.save(newCart);
                });
    }

    private CartProduct createCartProduct(Cart cart, AddCartProductRequestDto requestDto, Long userId) {
        CartProduct cartProduct = CartProduct.builder()
                .productId(requestDto.getProductId())
                .cart(cart)
                .productName(requestDto.getProductName())
                .productPrice(requestDto.getProductPrice())
                .productQuantity(requestDto.getProductQuantity())
                .productType(requestDto.getProductType())
                .build();

        cartProduct.updateCreateData(userId);  // TODO: AuditAware 적용 후 삭제
        return cartProduct;
    }

    private Cart findCartByConsumerId(UUID consumerId) {
        return cartRepository.findByConsumerId(consumerId)
                .orElseThrow(()-> new CartNotFoundException(CART_NOT_FOUND_EXCEPTION_MESSAGE));
    }

    private Cart findCartByCartId(UUID cartId) {
        return cartRepository.findByCartId(cartId)
                .orElseThrow(()-> new CartNotFoundException(CART_NOT_FOUND_EXCEPTION_MESSAGE));
    }
}
