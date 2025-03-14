package com.owl_express.ai.presentation;

import com.owl_express.ai.application.AiService;
import com.owl_express.ai.application.dtos.CommonDto;
import com.owl_express.ai.application.dtos.MessageCreateRequestDto;
import com.owl_express.ai.application.dtos.MessageCreateResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AiController {
    private final AiService aiService;

    @PostMapping("/messages/hub")
    public ResponseEntity<CommonDto<MessageCreateResponseDto>> createMessagesForHubDeliver(
            @Valid @RequestBody MessageCreateRequestDto requestDto
    ) {
        MessageCreateResponseDto responseDto = aiService.createMessageForHubDeliver(requestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                CommonDto.<MessageCreateResponseDto>builder()
                        .status(HttpStatus.CREATED)
                        .message("메세지 생성 완료")
                        .code(HttpStatus.CREATED.value())
                        .data(responseDto)
                        .build());
    }

    @PostMapping("/messages/company")
    public ResponseEntity<CommonDto<MessageCreateResponseDto>> createMessagesForCompanyDeliver(
            @Valid @RequestBody MessageCreateRequestDto requestDto
    ) {
        MessageCreateResponseDto responseDto = aiService.createMessageForCompanyDeliver(requestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                CommonDto.<MessageCreateResponseDto>builder()
                        .status(HttpStatus.CREATED)
                        .message("메세지 생성 완료")
                        .code(HttpStatus.CREATED.value())
                        .data(responseDto)
                        .build());
    }
}
