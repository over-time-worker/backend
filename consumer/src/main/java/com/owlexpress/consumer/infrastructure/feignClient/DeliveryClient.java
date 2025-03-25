package com.owlexpress.consumer.infrastructure.feignClient;

import com.owlexpress.consumer.common.dto.CommonDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "delivery-service")
public interface DeliveryClient {

    @GetMapping("api/delivery/{consumer_id}/consumer")
    CommonDto<Boolean> findByConsumer(
            @PathVariable("consumer_id") UUID consumerId
    );

}
