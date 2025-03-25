package com.owlexpress.deliverymanager.infrastructure.feignClient;

import com.owlexpress.deliverymanager.common.dto.response.HubFindResponseDto;
import com.owlexpress.deliverymanager.common.dto.CommonDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "hub-service")
public interface HubClient {


    @GetMapping("api/hub/{hubId}")
    public CommonDto<HubFindResponseDto> find(@PathVariable("hubId") UUID hubId);
}
