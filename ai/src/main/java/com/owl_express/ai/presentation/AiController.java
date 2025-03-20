package com.owl_express.ai.presentation;

import static com.owl_express.ai.presentation.ApiResponseMessageConstant.CREATE_MESSAGE_SUCCESS;
import static com.owl_express.ai.presentation.ApiResponseMessageConstant.FIND_MESSAGE_SUCCESS;
import static com.owl_express.ai.presentation.ApiResponseMessageConstant.SEARCH_MESSAGE_SUCCESS;

import com.owl_express.ai.application.dtos.CommonDto;
import com.owl_express.ai.application.dtos.request.CompanyDeliverMessageCreateRequestDto;
import com.owl_express.ai.application.dtos.request.HubDeliverMessageCreateRequestDto;
import com.owl_express.ai.application.dtos.response.MessageCreateResponseDto;
import com.owl_express.ai.application.dtos.response.MessageFindResponseDto;
import com.owl_express.ai.application.service.AiService;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AiController {
    private final AiService aiService;

    @PostMapping("/messages/hub")
    public ResponseEntity<CommonDto<MessageCreateResponseDto>> createMessagesForHubDeliver(
            @RequestBody HubDeliverMessageCreateRequestDto hubDeliverMessageCreateRequestDto
    ) {
        MessageCreateResponseDto responseDto = aiService.createMessageForHubDeliver(hubDeliverMessageCreateRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                CommonDto.<MessageCreateResponseDto>builder()
                        .status(HttpStatus.CREATED)
                        .message(CREATE_MESSAGE_SUCCESS)
                        .code(HttpStatus.CREATED.value())
                        .data(responseDto)
                        .build());
    }

    @PostMapping("/messages/company")
    public ResponseEntity<CommonDto<MessageCreateResponseDto>> createMessagesForCompanyDeliver(
            @RequestBody CompanyDeliverMessageCreateRequestDto companyDeliverMessageCreateRequestDto
    ) {
        MessageCreateResponseDto responseDto = aiService.createMessageForCompanyDeliver(companyDeliverMessageCreateRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                CommonDto.<MessageCreateResponseDto>builder()
                        .status(HttpStatus.CREATED)
                        .message(CREATE_MESSAGE_SUCCESS)
                        .code(HttpStatus.CREATED.value())
                        .data(responseDto)
                        .build());
    }

    @GetMapping("/messages/{ai_id}")
    public ResponseEntity<CommonDto<MessageFindResponseDto>> find(
            @NotNull(message = "[notNull:ai_id]") @PathVariable("ai_id") UUID aiId
    ) {
        MessageFindResponseDto responseDto = aiService.find(aiId);

        return ResponseEntity.status(HttpStatus.OK).body(
                CommonDto.<MessageFindResponseDto>builder()
                        .status(HttpStatus.CREATED)
                        .message(FIND_MESSAGE_SUCCESS)
                        .code(HttpStatus.OK.value())
                        .data(responseDto)
                        .build());
    }

    @GetMapping("/messages/search")
    public ResponseEntity<CommonDto<PagedModel<MessageFindResponseDto>>> search(
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "sort", defaultValue = "CREATEDAT") String sort,
            @RequestParam(name = "order_by", defaultValue = "ASC") String orderBy,
            @RequestParam(name = "q", required = false) UUID keyword
    ) {
        PagedModel<MessageFindResponseDto> pagedModel = aiService.search(page, size, sort, orderBy, keyword);

        return ResponseEntity.status(HttpStatus.OK).body(
                CommonDto.<PagedModel<MessageFindResponseDto>>builder()
                        .status(HttpStatus.OK)
                        .message(SEARCH_MESSAGE_SUCCESS)
                        .code(HttpStatus.OK.value())
                        .data(pagedModel)
                        .build());
    }
}
