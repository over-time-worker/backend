package com.owlexpress.consumer.domain.service;

import com.owlexpress.consumer.presentation.dto.response.ConsumerResponseDto;
import com.owlexpress.consumer.presentation.dto.response.SearchConsumerResponseDto;
import org.springframework.data.web.PagedModel;

import java.util.UUID;

public interface ConsumerService {
    ConsumerResponseDto find(UUID consumerId);

    PagedModel<SearchConsumerResponseDto> search(
            Integer page,
            Integer size,
            String sort,
            String q,
            String orderBy
    );
}
