package com.owlexpress.producer.infrastructure.feignClient;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "hub-service")
public interface HubFeignClient {

    //HubId로 조회하는 API
}
