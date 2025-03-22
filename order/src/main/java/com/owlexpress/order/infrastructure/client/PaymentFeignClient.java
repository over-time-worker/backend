package com.owlexpress.order.infrastructure.client;

import com.owlexpress.order.application.dto.request.CreatePaymentRequestDto;
import com.owlexpress.order.common.dto.CommonDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "payment-service")
public interface PaymentFeignClient {
    // 결제 시도 API
    @PostMapping("/api/payments")
    CommonDto<Void> sendPaymentRequest(
            @RequestHeader("X-User-Passport") String passport,
            @RequestBody CreatePaymentRequestDto request
    );
}
