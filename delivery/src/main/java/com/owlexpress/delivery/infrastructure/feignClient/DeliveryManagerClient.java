package com.owlexpress.delivery.infrastructure.feignClient;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name="delivery-manager-service")
public interface DeliveryManagerClient {

}
