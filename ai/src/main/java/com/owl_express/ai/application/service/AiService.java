package com.owl_express.ai.application.service;

import com.owl_express.ai.application.dtos.request.CompanyDeliverMessageCreateRequestDto;
import com.owl_express.ai.application.dtos.request.HubDeliverMessageCreateRequestDto;
import com.owl_express.ai.application.dtos.response.MessageCreateResponseDto;
import com.owl_express.ai.application.dtos.response.MessageFindResponseDto;
import java.util.UUID;
import org.springframework.data.web.PagedModel;

public interface AiService {
    MessageCreateResponseDto createMessageForHubDeliver(
            HubDeliverMessageCreateRequestDto hubDeliverMessageCreateRequestDto,
            String passport);

    MessageCreateResponseDto createMessageForCompanyDeliver(
            CompanyDeliverMessageCreateRequestDto companyDeliverMessageCreateRequestDto,
            String passport);

    MessageFindResponseDto find(UUID aiId);

    PagedModel<MessageFindResponseDto> search(int page, int size, String sort, String orderBy, UUID keyword);
}
