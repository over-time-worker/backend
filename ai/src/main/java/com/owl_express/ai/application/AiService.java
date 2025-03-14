package com.owl_express.ai.application;

import com.owl_express.ai.application.dtos.MessageCreateRequestDto;
import com.owl_express.ai.application.dtos.MessageCreateResponseDto;

public interface AiService {
    MessageCreateResponseDto createMessageForHubDeliver(MessageCreateRequestDto messageCreateRequestDto);

    public MessageCreateResponseDto createMessageForCompanyDeliver(MessageCreateRequestDto messageCreateRequestDto);
}
