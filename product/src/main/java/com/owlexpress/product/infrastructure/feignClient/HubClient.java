package com.owlexpress.product.infrastructure.feignClient;

import com.owlexpress.product.common.CommonDto;
import com.owlexpress.product.common.dto.request.HubProductCreateRequestDto;
import com.owlexpress.product.common.dto.request.HubProductUpdateRequestDto;
import com.owlexpress.product.common.dto.response.HubProductFindResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@FeignClient(name = "hub-service")
public interface HubClient {
    //허브 상품 추가
    @PostMapping("api/hub/product")
     ResponseEntity<CommonDto<Void>> create(
            @RequestHeader("X-User-Passport") String passport,
            @RequestBody HubProductCreateRequestDto requestDto
    );

    @GetMapping("api/hub/product/{productId}/product")
    CommonDto<HubProductFindResponseDto> findHubProduct(
            @PathVariable("productId") UUID productId
    );

    @PutMapping("api/hub/product")
    CommonDto<HubProductUpdateRequestDto> update(
            @RequestHeader("X-User-Passport") String passport,
            @RequestBody HubProductUpdateRequestDto requestDto
    );

    //허브 상품 재고 조회
    //TODO:: 허브 상품 재고 조회해서 hub-info에 등록하는 API 요청
}
