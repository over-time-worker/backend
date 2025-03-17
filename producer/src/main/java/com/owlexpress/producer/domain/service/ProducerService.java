package com.owlexpress.producer.domain.service;


import com.owlexpress.producer.presentation.dto.response.ProducerResponseDto;

import java.util.UUID;

public interface ProducerService {
    ProducerResponseDto find(UUID producerId);
}
