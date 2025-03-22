package com.owlexpress.deliverymanager.infrastructure.feignClient;

import com.owlexpress.deliverymanager.application.dto.response.AlarmCreateResponseDto;
import com.owlexpress.deliverymanager.common.dto.request.AlarmCreateRequestDto;
import com.owlexpress.deliverymanager.infrastructure.CommonDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient("alarm-service")
public interface AlarmClient {

    @PostMapping("api/alarms/hub-delivery")
    CommonDto<AlarmCreateResponseDto> createAlarmForHubDeliver(
            @RequestBody AlarmCreateRequestDto alarmCreateRequestDto,
            @RequestHeader("X-User-Passport") String passport
    );

    @PostMapping("api/alarms/company-delivery")
     CommonDto<AlarmCreateResponseDto> createAlarmForCompanyDeliver(
            @RequestBody AlarmCreateRequestDto alarmCreateRequestDto,
            @RequestHeader("X-User-Passport") String passport
    );
}
