package com.owlexpress.product.infrastructure.feignClient;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "hub-service")
public interface HubClient {

    //허브 상품 재고 조회
    //TODO:: 허브 상품 재고 조회해서 hub-info에 등록하는 API 요청
}
