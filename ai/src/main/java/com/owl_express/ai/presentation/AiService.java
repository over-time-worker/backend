package com.owl_express.ai.presentation;

import com.owl_express.ai.common.dto.request.CompanyDeliverMessageCreateRequestDto;
import com.owl_express.ai.common.dto.request.HubDeliverMessageCreateRequestDto;
import com.owl_express.ai.common.dto.response.MessageCreateResponseDto;
import com.owl_express.ai.common.dto.response.MessageFindResponseDto;
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

    void delete(UUID aiId, String passport);
}
