package com.owlexpress.consumer.infrastructure.feignClient;

import com.owlexpress.consumer.common.dto.CommonDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.UUID;

@FeignClient(name = "cart-service")
public interface CartClient {

    @DeleteMapping("api/carts/remove/{consumerId}")
    CommonDto<Void> deleteCart(
            @RequestHeader("X-User-Passport") String passport,
            @PathVariable("consumerId") UUID consumerId
    );
}
