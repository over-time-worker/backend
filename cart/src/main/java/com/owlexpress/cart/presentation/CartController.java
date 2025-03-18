package com.owlexpress.cart.presentation;

import static com.owlexpress.cart.presentation.dto.ApiResponseMessageConstant.ADD_CART_PRODUCT_SUCCESS_MESSAGE;
import static com.owlexpress.cart.presentation.dto.ApiResponseMessageConstant.CART_DELETE_SUCCESS_MESSAGE;
import static com.owlexpress.cart.presentation.dto.ApiResponseMessageConstant.CART_PRODUCT_DECREASE_SUCCESS_MESSAGE;
import static com.owlexpress.cart.presentation.dto.ApiResponseMessageConstant.CART_PRODUCT_DELETE_SUCCESS_MESSAGE;
import static com.owlexpress.cart.presentation.dto.ApiResponseMessageConstant.CART_PRODUCT_INCREASE_SUCCESS_MESSAGE;
import static com.owlexpress.cart.presentation.dto.ApiResponseMessageConstant.FIND_CART_PRODUCT_SUCCESS_MESSAGE;

import com.owlexpress.cart.application.service.CartService;
import com.owlexpress.cart.common.CommonDto;
import com.owlexpress.cart.presentation.dto.request.AddCartProductRequestDto;
import com.owlexpress.cart.presentation.dto.response.CartResponseDto;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/carts")
public class CartController {
    private final CartService cartService;

    @PostMapping("/{consumerId}")
    public ResponseEntity<CommonDto<Void>> create(
            @PathVariable("consumerId") UUID consumerId,
            @RequestBody AddCartProductRequestDto addCartProductRequestDto
    ){
        // TODO : gateway passport userId 적용
        Long userId = 1L;
        cartService.create(consumerId, addCartProductRequestDto, userId);

        CommonDto<Void> commonDto = CommonDto
                .<Void>builder()
                .status(HttpStatus.CREATED)
                .code(HttpStatus.CREATED.value())
                .message(ADD_CART_PRODUCT_SUCCESS_MESSAGE)
                .data(null)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(commonDto);
    }

    @GetMapping("/{consumerId}")
    public ResponseEntity<CommonDto<CartResponseDto>> find(
            @PathVariable("consumerId") UUID consumerId
    ) {

        CartResponseDto cartResponseDto = cartService.find(consumerId);

        CommonDto<CartResponseDto> commonDto = CommonDto
                .<CartResponseDto>builder()
                .status(HttpStatus.OK)
                .code(HttpStatus.OK.value())
                .message(FIND_CART_PRODUCT_SUCCESS_MESSAGE)
                .data(cartResponseDto)
                .build();

        return ResponseEntity.ok().body(commonDto);
    }

    @PatchMapping("/{cartId}/increase/{cartProductId}")
    public ResponseEntity<CommonDto<Void>> increase(
            @PathVariable("cartId") UUID cartId,
            @PathVariable("cartProductId") UUID cartProductId
    ) {
        Long userId = 1L;
        cartService.increase(cartId, cartProductId, userId);

        CommonDto<Void> commonDto = CommonDto
                .<Void>builder()
                .status(HttpStatus.ACCEPTED)
                .code(HttpStatus.ACCEPTED.value())
                .message(CART_PRODUCT_INCREASE_SUCCESS_MESSAGE)
                .data(null)
                .build();

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(commonDto);
    }

    @PatchMapping("/{cartId}/decrease/{cartProductId}")
    public ResponseEntity<CommonDto<Void>> decrease(
            @PathVariable("cartId") UUID cartId,
            @PathVariable("cartProductId") UUID cartProductId
    ) {
        Long userId = 1L;
        cartService.decrease(cartId, cartProductId, userId);

        CommonDto<Void> commonDto = CommonDto
                .<Void>builder()
                .status(HttpStatus.ACCEPTED)
                .code(HttpStatus.ACCEPTED.value())
                .message(CART_PRODUCT_DECREASE_SUCCESS_MESSAGE)
                .data(null)
                .build();

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(commonDto);
    }

    @DeleteMapping("/{cartId}/{cartProductId}")
    public ResponseEntity<CommonDto<Void>> deleteCartProduct(
            @PathVariable("cartId") UUID cartId,
            @PathVariable("cartProductId") UUID cartProductId
    ) {
        Long userId = 1L;
        cartService.deleteCartProduct(cartId, cartProductId, userId);

        CommonDto<Void> commonDto = CommonDto
                .<Void>builder()
                .status(HttpStatus.ACCEPTED)
                .code(HttpStatus.ACCEPTED.value())
                .message(CART_PRODUCT_DELETE_SUCCESS_MESSAGE)
                .data(null)
                .build();

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(commonDto);
    }

    @DeleteMapping("/{cartId}")
    public ResponseEntity<CommonDto<Void>> deleteCart(
            @PathVariable("cartId") UUID cartId
    ) {
        // TODO : 장바구니 삭제는 MASTER의 consumer 계정 삭제시 수행
        Long userId = 1L;
        cartService.deleteCart(cartId, userId);

        CommonDto<Void> commonDto = CommonDto
                .<Void>builder()
                .status(HttpStatus.ACCEPTED)
                .code(HttpStatus.ACCEPTED.value())
                .message(CART_DELETE_SUCCESS_MESSAGE)
                .data(null)
                .build();

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(commonDto);
    }
}
