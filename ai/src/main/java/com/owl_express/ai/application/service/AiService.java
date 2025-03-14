package com.owl_express.ai.application.service;

import com.owl_express.ai.application.dtos.request.MessageCreateRequestDto;
import com.owl_express.ai.application.dtos.response.MessageCreateResponseDto;
import com.owl_express.ai.application.dtos.response.MessageFindResponseDto;
import java.util.UUID;

public interface AiService {
    MessageCreateResponseDto createMessageForHubDeliver(MessageCreateRequestDto messageCreateRequestDto);

    MessageCreateResponseDto createMessageForCompanyDeliver(MessageCreateRequestDto messageCreateRequestDto);

    MessageFindResponseDto findMessage(UUID aiId);
}
