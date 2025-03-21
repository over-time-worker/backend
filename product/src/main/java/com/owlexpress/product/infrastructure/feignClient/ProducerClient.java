package com.owlexpress.product.infrastructure.feignClient;

import com.owlexpress.product.common.CommonDto;
import com.owlexpress.product.common.dto.request.CreateProductInfoRequestDto;
import com.owlexpress.product.common.dto.response.ProducerResponseDto;
import com.owlexpress.product.common.dto.request.UpdateProductInfoRequestDto;
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
            @RequestHeader("X-User-Passport") String passport,
            @Valid @RequestBody CreateProductInfoRequestDto createProductInfoRequestDto
    );

    @GetMapping(REQUEST_PREFIX+"/{producerId}")
     CommonDto<ProducerResponseDto> find(
            @PathVariable UUID producerId
    );

    @PutMapping(REQUEST_PREFIX+"/product-info/{productId}")
     ResponseEntity<CommonDto<Void>> update(
            @RequestHeader("X-User-Passport") String passport,
            @PathVariable UUID productId,
            @Valid @RequestBody UpdateProductInfoRequestDto updateProductInfoRequestDto
    );

    @DeleteMapping(REQUEST_PREFIX+"/product-info/{productId}")
    ResponseEntity<CommonDto<Void>> delete(
            @RequestHeader("X-User-Passport") String passport,
            @PathVariable UUID productId
    );
}
