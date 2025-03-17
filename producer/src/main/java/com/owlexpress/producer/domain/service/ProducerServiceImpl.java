package com.owlexpress.producer.domain.service;

import com.owlexpress.producer.common.helper.ProducerHelper;
import com.owlexpress.producer.domain.entity.Producer;
import com.owlexpress.producer.domain.repository.ProducerRepository;
import com.owlexpress.producer.presentation.dto.response.ProducerResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProducerServiceImpl implements ProducerService {
    private final ProducerRepository producerRepository;
    private final ProducerHelper producerHelper;

    @Override
    public ProducerResponseDto find(UUID producerId) {

        Producer producer = producerHelper.getProducer(producerId);

        return ProducerResponseDto.fromEntity(producer);
    }
}
