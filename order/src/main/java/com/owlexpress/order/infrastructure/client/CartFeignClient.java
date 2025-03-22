package com.owlexpress.order.infrastructure.client;

import com.owlexpress.order.common.dto.CommonDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;

@FeignClient(name = "cart-service")
public interface CartFeignClient {
    // 주문 성공시 장바구니 상품 삭제
    @DeleteMapping("/api/carts")
    CommonDto<Void> deleteCartProductsFromOrder();
}
