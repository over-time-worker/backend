package com.owlexpress.delivery.infrastructure.feignClient;

import com.owlexpress.delivery.application.dtos.CommonDto;
import com.owlexpress.delivery.application.dtos.request.DeliveryManagerRequestDto;
import com.owlexpress.delivery.application.dtos.response.AlarmCreateResponseDto;
import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name="deliveryManager-service")
public interface DeliveryManagerClient {

    @PostMapping("/api/hub/delivery/assign")
    CommonDto<AlarmCreateResponseDto> assignHub(
            @RequestHeader("X-User-Passport") String passport,
            DeliveryManagerRequestDto deliveryManagerRequestDto
    );

    @PutMapping( "/api/hub/delivery/return-hub/{deliveryManagerId}")
    void returnHub(@PathVariable("deliveryManagerId") UUID deliveryManagerId);

    @PostMapping("/api/consumer/delivery/assign")
    CommonDto<AlarmCreateResponseDto> assignCompany(
            @RequestHeader("X-User-Passport") String passport,
            DeliveryManagerRequestDto deliveryManagerRequestDto
    );

    @PutMapping( "/api/consumer/delivery/return-hub/{deliveryManagerId}")
    void returnCompany(
            @RequestHeader(name = "X-User-Passport") String passport,
            @PathVariable("deliveryManagerId") UUID deliveryManagerId
    );


}
