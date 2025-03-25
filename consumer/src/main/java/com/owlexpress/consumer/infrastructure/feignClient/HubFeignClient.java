package com.owlexpress.consumer.infrastructure.feignClient;

import com.owlexpress.consumer.common.dto.CommonDto;
import com.owlexpress.consumer.common.dto.response.HubFindResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "hub-service")
public interface HubFeignClient {

    @GetMapping("api/hub/{hubId}")
    CommonDto<HubFindResponseDto> find(@PathVariable("hubId") UUID hubId);
}
