package com.owlexpress.producer.domain.service;


import com.owlexpress.producer.presentation.dto.response.ProducerResponseDto;
import com.owlexpress.producer.presentation.dto.response.SearchProducerResponseDto;
import org.springframework.data.web.PagedModel;

import java.util.UUID;

public interface ProducerService {
    ProducerResponseDto find(UUID producerId);

    PagedModel<SearchProducerResponseDto> search(
            Integer page,
            Integer size,
            String sort,
            String q,
            String orderBy
    );
}
