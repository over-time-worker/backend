package com.owlexpress.cart.application.service;

import static com.owlexpress.cart.common.exception.ExceptionMessage.CART_NOT_FOUND_EXCEPTION_MESSAGE;

import com.owlexpress.cart.common.dto.request.HubProductIsEnoughRequestDto;
import com.owlexpress.cart.common.dto.response.HubProductIsEnoughResponseDto;
import com.owlexpress.cart.application.exception.CartNotFoundException;
import com.owlexpress.cart.common.dto.CommonDto;
import com.owlexpress.cart.common.dto.PassportDto;
import com.owlexpress.cart.common.helper.PassportHelper;
import com.owlexpress.cart.domain.CartRepository;
import com.owlexpress.cart.domain.entity.Cart;
import com.owlexpress.cart.domain.entity.CartProduct;
import com.owlexpress.cart.infrastructure.client.HubProductFeignClient;
import com.owlexpress.cart.presentation.CartService;
import com.owlexpress.cart.common.dto.request.AddCartProductRequestDto;
import com.owlexpress.cart.common.dto.request.CartProductDeleteRequestDto;
import com.owlexpress.cart.common.dto.response.CartResponseDto;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final HubProductFeignClient hubProductFeignClient;
    private final PassportHelper passportHelper;

    @Transactional
    @Override
    public void create(
            UUID consumerId,
            AddCartProductRequestDto addCartProductRequestDto,
            String passport
    ) {
        // TODO : 해당 장바구니 상품이 이미 존재한다면 수량 1 증가
        PassportDto passportDto = getPassport(passport);

        Cart cart = findOrCreateCart(
                consumerId,
                passportDto.getUserId(),
                addCartProductRequestDto.getProductPrice()
        );

        CartProduct cartProduct = createCartProduct(
                cart,
                addCartProductRequestDto,
                passportDto.getUserId()
        );

        cart.addCartProduct(cartProduct);
        cart.updateModifiedData(passportDto.getUserId());  // TODO: AuditAware 적용 후 삭제

        cartRepository.save(cart);
    }

    @Transactional
    @Override
    public void increase(UUID cartId, UUID cartProductId, String passport) {
        PassportDto passportDto = getPassport(passport);
        Cart cart = findCartByCartId(cartId);
        cart.increaseCartProductQuantity(cartProductId, passportDto.getUserId());
    }

    @Transactional
    @Override
    public void decrease(UUID cartId, UUID cartProductId, String passport) {
        PassportDto passportDto = getPassport(passport);
        Cart cart = findCartByCartId(cartId);
        cart.decreaseCartProductQuantity(cartProductId, passportDto.getUserId());
    }

    @Transactional
    @Override
    public void deleteCartProduct(UUID cartId, UUID cartProductId, String passport) {
        PassportDto passportDto = getPassport(passport);
        Cart cart = findCartByCartId(cartId);
        cart.removeCartProduct(cartProductId, passportDto.getUserId());
    }

    @Transactional
    @Override
    public void deleteCart(UUID consumerId, String passport) {
        PassportDto passportDto = getPassport(passport);
        Cart cart = findCartByConsumerId(consumerId);
        cart.removeCart(passportDto.getUserId());
    }

    @Transactional
    @Override
    public CartResponseDto find(String passport, UUID consumerId) {
        // 1. 재고가 있으면 상품을 그대로 반환
        // 2. 재고가 없다면 상품을 품절상태(isSoldOut = true)로 변경
        PassportDto passportDto = getPassport(passport);
        Cart cart = findCartByConsumerId(consumerId);

        CommonDto<List<HubProductIsEnoughResponseDto>> hubProductStock = hubProductFeignClient
                .findHubProductStock(
                        passport,
                        cart.getCartProductList()
                        .stream()
                        .map(cp -> HubProductIsEnoughRequestDto
                                .builder()
                                .hubProductId(cp.getProductId())
                                .build()
                        )
                        .toList()
        );

        hubProductStock.getData().forEach(stockInfo -> cart.getCartProductList().stream()
                .filter(cartProduct -> cartProduct
                        .getProductId().equals(stockInfo.getHubProductId()))
                .forEach(cartProduct -> {
                    boolean isSoldOut = !stockInfo.isEnough();
                    if (isSoldOut) {
                        cartProduct.setIsSoldOut(isSoldOut);
                    }
                }));

        return CartResponseDto.builder()
                .cart(cart)
                .cartProducts(cart.getCartProductList())
                .build();
    }

    @Override
    public void deleteCartProductsFromOrder(
            UUID consumerId,
            String passport,
            CartProductDeleteRequestDto requestDto
    ) {
        PassportDto passportDto = getPassport(passport);

        Cart cart = findCartByConsumerId(consumerId);

        requestDto.getProductIds()
                        .forEach(productId -> cart
                                .removeCartProduct(productId, passportDto.getUserId()));
    }

    private PassportDto getPassport(String passport){
        return passportHelper.getPassportDto(passport);
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
                .isSoldOut(false)
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
