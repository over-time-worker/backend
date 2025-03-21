package com.owlexpress.producer.infrastructure.feignClient;

import com.owlexpress.producer.common.CommonDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.UUID;

@FeignClient("hub-service")
public interface HubProductClient {

    @DeleteMapping("api/hub/product/{hubProductId}")
     ResponseEntity<CommonDto<Boolean>> delete(
            @RequestHeader("X-User-Passport") String passport,
            @PathVariable("hubProductId") UUID hubProductId
    );
}
