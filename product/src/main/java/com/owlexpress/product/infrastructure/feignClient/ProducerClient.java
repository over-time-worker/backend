package com.owlexpress.product.infrastructure.feignClient;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "producer-service")
public interface ProducerClient {

    //생산업체 정보 조회 API연결
}
