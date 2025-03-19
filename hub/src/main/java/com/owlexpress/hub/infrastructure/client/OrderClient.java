package com.owlexpress.hub.infrastructure.client;

import com.owlexpress.hub.application.dto.response.OrderSearchResponseDto;
import com.owlexpress.hub.presentation.dto.CommonDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.web.PagedModel;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.UUID;

@FeignClient("order-service")
public interface OrderClient {

    @GetMapping("/api/orders/search?size=50")
    CommonDto<PagedModel<OrderSearchResponseDto>> orderSearch(
            @RequestParam("startDate") LocalDateTime startDate,
            @RequestParam(value = "page") int page
    );

    @DeleteMapping("/api/orders/{orderId}")
    CommonDto<Void> deleteOrder(@PathVariable("orderId") UUID orderId);
}
