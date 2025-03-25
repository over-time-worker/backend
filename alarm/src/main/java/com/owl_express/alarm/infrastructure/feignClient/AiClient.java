package com.owl_express.alarm.infrastructure.feignClient;

import com.owl_express.alarm.common.dto.CommonDto;
import com.owl_express.alarm.common.dto.request.CompanyDeliverMessageCreateRequestDto;
import com.owl_express.alarm.common.dto.request.HubDeliverMessageCreateRequestDto;
import com.owl_express.alarm.common.dto.response.MessageCreateResponseDto;
import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name="ai-service")
public interface AiClient {
    @PostMapping("api/ai/messages/hub")
    CommonDto<MessageCreateResponseDto> createMessagesForHubDeliver(
            HubDeliverMessageCreateRequestDto requestDto,
            @RequestHeader("X-User-Passport") String passport
    );

    @PostMapping( "api/ai/messages/company")
    CommonDto<MessageCreateResponseDto> createMessagesForCompanyDeliver(
            CompanyDeliverMessageCreateRequestDto requestDto,
            @RequestHeader("X-User-Passport") String passport
    );

    @DeleteMapping("api/ai/messages/{ai_id}")
    CommonDto<Void> delete(
             @PathVariable("ai_id") UUID aiId,
            @RequestHeader("X-User-Passport") String passport
    );
}
