package com.owlexpress.producer.infrastructure.feignClient;

import com.owlexpress.producer.application.dto.request.UpdateProductDto;
import com.owlexpress.producer.common.CommonDto;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

@FeignClient(name = "product-service")
public interface ProductClient {

    @DeleteMapping("/products/{productsId}")
     CommonDto<Void> delete(
            @PathVariable UUID productsId
    );

    @PutMapping("/products/{productsId}")
    ResponseEntity<CommonDto<Void>> update(
            //TODO:: gateway 반환 유저 데이터 @RequestHeader("X-User-Passport") String passport,
            @Valid @RequestBody UpdateProductDto updateProductDto,
            @PathVariable UUID productsId
    );

}
