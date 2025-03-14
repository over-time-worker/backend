package com.owl_express.ai.application;

import com.owl_express.ai.application.dtos.MessageCreateRequestDto;
import com.owl_express.ai.application.dtos.MessageCreateResponseDto;
import com.owl_express.ai.application.dtos.MessageFindResponseDto;
import java.util.UUID;

public interface AiService {
    MessageCreateResponseDto createMessageForHubDeliver(MessageCreateRequestDto messageCreateRequestDto);

    MessageCreateResponseDto createMessageForCompanyDeliver(MessageCreateRequestDto messageCreateRequestDto);

    MessageFindResponseDto findMessage(UUID aiId);
}
