package com.owlexpress.delivery.infrastructure.feignClient;

import com.owlexpress.delivery.application.dtos.CommonDto;
import com.owlexpress.delivery.application.dtos.request.DeliveryManagerRequestDto;
import com.owlexpress.delivery.application.dtos.response.AlarmCreateResponseDto;
import com.owlexpress.delivery.infrastructure.config.FeignOkHttpConfiguration;
import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name="deliveryManager-service")
public interface DeliveryManagerClient {

    @PostMapping("/api/hub/delivery/assign")
    CommonDto<AlarmCreateResponseDto> assignHub(
            @RequestHeader("X-User-Passport") String passport,
            DeliveryManagerRequestDto deliveryManagerRequestDto
    );

    @RequestMapping(method = RequestMethod.PATCH, value = "/api/hub/delivery/return-hub/{deliveryManagerId}")
    void returnHub(@PathVariable("deliveryManagerId") UUID deliveryManagerId);

    @PostMapping("/api/consumer/delivery/assign")
    CommonDto<AlarmCreateResponseDto> assignCompany(
            @RequestHeader("X-User-Passport") String passport,
            DeliveryManagerRequestDto deliveryManagerRequestDto
    );

    @RequestMapping(method = RequestMethod.PATCH, value = "/api/consumer/delivery/return-hub/{deliveryManagerId}")
    void returnCompany(@PathVariable("deliveryManagerId") UUID deliveryManagerId);


}
