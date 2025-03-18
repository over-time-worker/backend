package com.owlexpress.cart.application.service;

import static com.owlexpress.cart.common.exception.ExceptionMessage.CART_NOT_FOUND_EXCEPTION_MESSAGE;

import com.owlexpress.cart.application.exception.CartNotFoundException;
import com.owlexpress.cart.domain.CartRepository;
import com.owlexpress.cart.domain.entity.Cart;
import com.owlexpress.cart.domain.entity.CartProduct;
import com.owlexpress.cart.presentation.dto.request.AddCartProductRequestDto;
import com.owlexpress.cart.presentation.dto.response.CartResponseDto;
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
        // 장바구니가 있으면 상품 추가, 없으면 장바구니 생성 + 상품 추가
        Cart cart = null;

        if (!cartRepository.existsByConsumerId(consumerId)) {
            Cart newCart = Cart.builder()
                    .consumerId(consumerId)
                    .totalPrice(addCartProductRequestDto.getProductPrice())
                    .build();

            // TODO : AuditAware 적용 후 삭제
            newCart.updateCreateData(userId);

            cart = cartRepository.save(newCart);
        }

        if (cart == null) {
            cart = findCartByConsumerId(consumerId);
        }

        CartProduct cartProduct = CartProduct.builder()
                .productId(addCartProductRequestDto.getProductId())
                .cart(cart)
                .productName(addCartProductRequestDto.getProductName())
                .productPrice(addCartProductRequestDto.getProductPrice())
                .productQuantity(addCartProductRequestDto.getProductQuantity())
                .productType(addCartProductRequestDto.getProductType())
                .build();

        // TODO : AuditAware 적용 후 삭제
        cartProduct.updateCreateData(userId);

        cart.addCartProduct(cartProduct);

        // TODO : AuditAware 적용 후 삭제
        cart.updateModifiedData(userId);

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

    private Cart findCartByConsumerId(UUID consumerId) {
        return cartRepository.findByConsumerId(consumerId)
                .orElseThrow(()-> new CartNotFoundException(CART_NOT_FOUND_EXCEPTION_MESSAGE));
    }

    private Cart findCartByCartId(UUID cartId) {
        return cartRepository.findByCartId(cartId)
                .orElseThrow(()-> new CartNotFoundException(CART_NOT_FOUND_EXCEPTION_MESSAGE));
    }
}
