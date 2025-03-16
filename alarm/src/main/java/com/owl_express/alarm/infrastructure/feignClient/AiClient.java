package com.owl_express.alarm.infrastructure.feignClient;

import com.owl_express.alarm.application.dtos.CommonDto;
import com.owl_express.alarm.application.dtos.request.MessageCreateRequestDto;
import com.owl_express.alarm.application.dtos.response.MessageCreateResponseDto;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name="ai-service")
public interface AiClient {
    @PostMapping("/messages/hub")
    ResponseEntity<CommonDto<MessageCreateResponseDto>> createMessagesForHubDeliver(
            MessageCreateRequestDto requestDto
    );

    @PostMapping("/messages/company")
    ResponseEntity<CommonDto<MessageCreateResponseDto>> createMessagesForCompanyDeliver(
            @Valid @RequestBody MessageCreateRequestDto requestDto
    );
}
