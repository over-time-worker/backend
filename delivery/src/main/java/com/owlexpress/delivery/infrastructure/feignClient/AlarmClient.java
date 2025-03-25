package com.owlexpress.delivery.infrastructure.feignClient;

import com.owlexpress.delivery.common.dto.CommonDto;
import com.owlexpress.delivery.common.dto.request.HubDeliverFallbackMessageCreateRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name="alarm-service")
public interface AlarmClient {

    @DeleteMapping("/api/alarms/channel/{channel_id}/message/{message_id}")
    CommonDto<Void> companyDelete(
            @PathVariable("channel_id") String channelId,
            @PathVariable("message_id") String messageId,
            @RequestHeader("X-User-Passport") String passport
    );

    @PostMapping("/hub/fallback")
    CommonDto<Void> hubFallback(
            HubDeliverFallbackMessageCreateRequestDto requestDto,
            @RequestHeader("X-User-Passport") String passport
    );
}

