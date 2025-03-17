package com.owl_express.alarm.infrastructure.feignClient;

import com.owl_express.alarm.application.dtos.response.OrderFindResponseDto;
import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "order-service")
public interface OrderClient {
    @GetMapping("/api/orders/{order_id}")
    OrderFindResponseDto findOrder(@PathVariable("order_id") UUID orderId);
}
