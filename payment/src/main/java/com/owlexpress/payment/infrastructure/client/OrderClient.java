package com.owlexpress.payment.infrastructure.client;

import com.owlexpress.payment.application.dto.response.OrderFindResponseDto;
import com.owlexpress.payment.presentation.dto.CommonDto;
import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("order-service")
public interface OrderClient {

    @GetMapping("/api/orders/{orderId}")
    public CommonDto<OrderFindResponseDto> findOrderDetails(@PathVariable("orderId") UUID orderId);
}
