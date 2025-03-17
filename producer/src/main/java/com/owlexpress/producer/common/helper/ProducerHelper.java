package com.owlexpress.producer.common.helper;

import com.owlexpress.producer.common.exception.ProducerException;
import com.owlexpress.producer.domain.entity.Producer;
import com.owlexpress.producer.domain.repository.ProducerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ProducerHelper {

    private final ProducerRepository producerRepository;

    public Producer getProducer(UUID producerId) {
        return producerRepository.findById(producerId).orElseThrow(
                ()-> new ProducerException.ProducerNotFoundException("찾는 영업이 존재하지 않습니다.")
        );
    }
}
