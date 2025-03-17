package com.owlexpress.producer.infrastructure.repository;

import com.owlexpress.producer.domain.entity.Producer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface ProducerQueryDslRepository {
    Page<Producer> searchProducer(
            String sort,
            String q,
            String orderBy,
            PageRequest pageRequest
    );
}
