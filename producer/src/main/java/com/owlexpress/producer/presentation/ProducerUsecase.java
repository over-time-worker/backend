package com.owlexpress.producer.presentation;

import com.owlexpress.producer.common.dto.request.CreateProducerRequestDto;
import com.owlexpress.producer.common.dto.request.UpdateProducerRequestDto;
import com.owlexpress.producer.domain.entity.Producer;
import java.util.UUID;
import org.springframework.transaction.annotation.Transactional;

public interface ProducerUsecase {

    @Transactional
    void create(
            CreateProducerRequestDto createProducerRequestDto,
            String passport
    );

    @Transactional
    void update(
            UpdateProducerRequestDto updateProducerRequestDto,
            UUID producerId,
            String passport
    );

    void updateProducer(
            Producer producer,
            UpdateProducerRequestDto dto
    );

    @Transactional
    void delete(
            UUID producerId,
            String passport
    );
}
