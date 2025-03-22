package com.owlexpress.payment.infrastructure.client;

import com.owlexpress.payment.application.dto.request.OptimalRouteRequestDto;
import com.owlexpress.payment.application.dto.response.RouteResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("hub-service")
public interface HubIntervalInfoClient {

    @PostMapping("/api/hub-interval-info/route")
    RouteResponseDto findOptimalPath(@RequestBody OptimalRouteRequestDto requestDto);
}
