package com.owlexpress.consumer.domain.service;

import com.owlexpress.consumer.common.exceptions.ConsumerException;
import com.owlexpress.consumer.domain.entity.Consumer;
import com.owlexpress.consumer.domain.repository.ConsumerRepository;
import com.owlexpress.consumer.presentation.dto.response.ConsumerResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ConsumerServiceImpl implements ConsumerService {
    private final ConsumerRepository consumerRepository;

    @Override
    @Transactional(readOnly = true)
    public ConsumerResponseDto find(UUID consumerId) {
        Consumer consumer = consumerRepository.findById(consumerId)
                                              .orElseThrow(() -> new ConsumerException.ConsumerNotFoundException("수령 업체를 찾을 수 없습니다."));

        return ConsumerResponseDto.fromEntity(consumer);
    }

}
