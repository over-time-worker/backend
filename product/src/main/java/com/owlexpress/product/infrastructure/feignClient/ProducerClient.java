package com.owlexpress.product.infrastructure.feignClient;

import com.owlexpress.product.common.CommonDto;
import com.owlexpress.product.common.dto.CreateProductInfoRequestDto;
import com.owlexpress.product.common.dto.ProducerResponseDto;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

@FeignClient(name = "producer-service")
public interface ProducerClient {

    @PostMapping("/api/producers/product-info")
     CommonDto<Void> create(
            //TODO:: gateway 반환 유저 데이터 @RequestHeader("X-User-Passport") String passport,
            @Valid @RequestBody CreateProductInfoRequestDto createProductInfoRequestDto
    );

    @GetMapping("/api/producers/{producerId}")
     CommonDto<ProducerResponseDto> find(
            @PathVariable UUID producerId
    );
}
