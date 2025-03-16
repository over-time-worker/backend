package com.owl_express.ai.application.service;

import com.owl_express.ai.application.dtos.request.MessageCreateRequestDto;
import com.owl_express.ai.application.dtos.response.MessageCreateResponseDto;
import com.owl_express.ai.application.dtos.response.MessageFindResponseDto;
import com.owl_express.ai.domain.entity.Ai;
import java.util.UUID;
import org.springframework.data.web.PagedModel;

public interface AiService {
    MessageCreateResponseDto createMessageForHubDeliver(MessageCreateRequestDto messageCreateRequestDto);

    MessageCreateResponseDto createMessageForCompanyDeliver(MessageCreateRequestDto messageCreateRequestDto);

    MessageFindResponseDto findMessage(UUID aiId);

    PagedModel<MessageFindResponseDto> searchMessages(int page, int size, String sort, String orderBy, UUID keyword);
}
