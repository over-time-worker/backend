package com.owlexpress.hub.infrastructure.client;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(value = "product-service")
public interface ProductClient {
}
