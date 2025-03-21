package com.owlexpress.order.infrastructure.client;

import com.owlexpress.order.application.dto.response.GetConsumerInfoResponseDto;
import com.owlexpress.order.common.dto.CommonDto;
import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "consumer-service")
public interface ConsumerFeignClient {
    @GetMapping("/api/consumers/{consumerId}")
    CommonDto<GetConsumerInfoResponseDto> getConsumerInfo (
            @RequestHeader("X-User-Passport") String passport,
            @PathVariable("consumerId") UUID consumerId
    );
}
