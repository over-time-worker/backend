package com.owl_express.alarm.infrastructure.feignClient;

import com.owl_express.alarm.application.dtos.CommonDto;
import com.owl_express.alarm.application.dtos.request.CompanyDeliverMessageCreateRequestDto;
import com.owl_express.alarm.application.dtos.request.HubDeliverMessageCreateRequestDto;
import com.owl_express.alarm.application.dtos.response.MessageCreateResponseDto;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name="ai-service")
public interface AiClient {
    @PostMapping("api/ai/messages/hub")
    CommonDto<MessageCreateResponseDto> createMessagesForHubDeliver(
            HubDeliverMessageCreateRequestDto requestDto
    );

    @PostMapping("api/ai/messages/company")
    CommonDto<MessageCreateResponseDto> createMessagesForCompanyDeliver(
            @Valid @RequestBody CompanyDeliverMessageCreateRequestDto requestDto
    );
}
