package com.owlexpress.cart.infrastructure.client;

import com.owlexpress.cart.common.dto.request.HubProductIsEnoughRequestDto;
import com.owlexpress.cart.common.dto.response.HubProductIsEnoughResponseDto;
import com.owlexpress.cart.common.dto.CommonDto;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "hub-service")
public interface HubProductFeignClient {

    @PostMapping("/api/hub/product/check-stock")
    CommonDto<List<HubProductIsEnoughResponseDto>> findHubProductStock(
            @RequestHeader("X-User-Passport") String passport,
            @RequestBody List<HubProductIsEnoughRequestDto> requestDtos
    );

}
