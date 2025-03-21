package com.owlexpress.producer.infrastructure.feignClient;

import com.owlexpress.producer.application.dto.request.UpdateProductDto;
import com.owlexpress.producer.common.CommonDto;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@FeignClient(name = "product-service")
public interface ProductClient {

    @DeleteMapping("api/products/{productsId}")
     CommonDto<Void> delete(
            @RequestHeader("X-User-Passport") String passport,
            @PathVariable UUID productsId
    );

    @PutMapping("api/products/{productsId}")
    ResponseEntity<CommonDto<Void>> update(
            @RequestHeader("X-User-Passport") String passport,
            @Valid @RequestBody UpdateProductDto updateProductDto,
            @PathVariable UUID productsId
    );

}
