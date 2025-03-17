package com.owlexpress.producer.infrastructure.feignClient;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "user-service")
public interface UserFeignClient {

    //User정보 조회
}
