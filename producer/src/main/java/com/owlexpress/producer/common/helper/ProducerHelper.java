package com.owlexpress.producer.common.helper;

import com.owlexpress.producer.common.exception.ProducerException;
import com.owlexpress.producer.domain.entity.Producer;
import com.owlexpress.producer.domain.repository.ProducerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static com.owlexpress.producer.common.exception.ExceptionMessage.PRODUCER_NOT_FOUND_MESSAGE;

@Component
@RequiredArgsConstructor
public class ProducerHelper {

    private final ProducerRepository producerRepository;

    public Producer getProducer(UUID producerId) {
        return producerRepository.findById(producerId).orElseThrow(
                ()-> new ProducerException.ProducerNotFoundException(PRODUCER_NOT_FOUND_MESSAGE)
        );
    }
}
