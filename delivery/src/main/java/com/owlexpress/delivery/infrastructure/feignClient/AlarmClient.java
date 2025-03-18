package com.owlexpress.delivery.infrastructure.feignClient;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name="alarm-service")
public interface AlarmClient {

}
