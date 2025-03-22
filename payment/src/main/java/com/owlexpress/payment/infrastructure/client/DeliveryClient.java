package com.owlexpress.payment.infrastructure.client;

import com.owlexpress.payment.application.dto.request.DeliveryCreateRequestDto;
import com.owlexpress.payment.presentation.dto.CommonDto;
import java.util.Map;
import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient("delivery-service")
public interface DeliveryClient {

    @PostMapping("/api/delivery")
    CommonDto<Map<String, UUID>> createDelivery(
            @RequestBody DeliveryCreateRequestDto createRequestDto,
            @RequestHeader("X-User-Passport") String passport
    );

}
