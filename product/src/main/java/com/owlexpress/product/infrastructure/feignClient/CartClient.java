package com.owlexpress.product.infrastructure.feignClient;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient("cart-service")
public interface CartClient {

}
