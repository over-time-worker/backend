package com.owlexpress.producer.infrastructure.feignClient;

import com.owlexpress.producer.common.CommonDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "product-service")
public interface ProductClient {

    @DeleteMapping("/{productsId}")
     CommonDto<Void> delete(
            @PathVariable UUID productsId
    );

}
