package com.owlexpress.order.infrastructure.client;

import com.owlexpress.order.application.dto.request.ConfirmHubStockRequestDto;
import com.owlexpress.order.application.dto.response.ConfirmHubStockResponseDto;
import com.owlexpress.order.common.dto.CommonDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient("hub-service")
public interface HubFeignClient {
    @PostMapping("/api/hub/product/confirm-stock")
    CommonDto<ConfirmHubStockResponseDto> findHubProductStock(
            @RequestHeader("X-User-Passport") String passport,
            @RequestBody ConfirmHubStockRequestDto requestDtos
    );
}
