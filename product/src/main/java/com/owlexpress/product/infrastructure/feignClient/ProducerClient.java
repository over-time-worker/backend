package com.owlexpress.product.infrastructure.feignClient;

import com.owlexpress.product.common.CommonDto;
import com.owlexpress.product.common.dto.CreateProductInfoRequestDto;
import com.owlexpress.product.common.dto.ProducerResponseDto;
import com.owlexpress.product.common.dto.UpdateProductInfoRequestDto;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@FeignClient(name = "producer-service")
public interface ProducerClient {
    String REQUEST_PREFIX = "/api/producers";

    @PostMapping(REQUEST_PREFIX+"/product-info")
     CommonDto<Void> create(
            //TODO:: gateway 반환 유저 데이터 @RequestHeader("X-User-Passport") String passport,
            @Valid @RequestBody CreateProductInfoRequestDto createProductInfoRequestDto
    );

    @GetMapping(REQUEST_PREFIX+"/{producerId}")
     CommonDto<ProducerResponseDto> find(
            @PathVariable UUID producerId
    );

    @PutMapping(REQUEST_PREFIX+"/product-info/{productId}")
     ResponseEntity<CommonDto<Void>> update(
            //TODO:: gateway 반환 유저 데이터 @RequestHeader("X-User-Passport") String passport,
            @PathVariable UUID productId,
            @Valid @RequestBody UpdateProductInfoRequestDto updateProductInfoRequestDto
    );

    @DeleteMapping(REQUEST_PREFIX+"/product-info/{productId}")
    public ResponseEntity<CommonDto<Void>> delete(
            //TODO:: gateway 반환 유저 데이터 @RequestHeader("X-User-Passport") String passport,
            @PathVariable UUID productId
    );
}
