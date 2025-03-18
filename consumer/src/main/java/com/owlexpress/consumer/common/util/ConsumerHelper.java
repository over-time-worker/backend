package com.owlexpress.consumer.common.util;

import com.owlexpress.consumer.common.exceptions.ConsumerException;
import com.owlexpress.consumer.domain.entity.Consumer;
import com.owlexpress.consumer.domain.repository.ConsumerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ConsumerHelper {
    private final ConsumerRepository consumerRepository;

    public Consumer getConsumer(UUID consumerId) {
        return consumerRepository.findById(consumerId)
                          .orElseThrow(() -> new ConsumerException.ConsumerNotFoundException("찾으시는 수령업체가 존재하지 않습니다."));

    }

}
