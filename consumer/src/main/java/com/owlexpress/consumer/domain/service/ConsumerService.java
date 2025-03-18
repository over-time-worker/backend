package com.owlexpress.consumer.domain.service;

import com.owlexpress.consumer.presentation.dto.response.ConsumerResponseDto;

import java.util.UUID;

public interface ConsumerService {
    ConsumerResponseDto find(UUID consumerId);
}
