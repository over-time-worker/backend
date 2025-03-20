package com.owlexpress.order.infrastructure.client;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "payment-service")
public interface PaymentFeignClient {

}
