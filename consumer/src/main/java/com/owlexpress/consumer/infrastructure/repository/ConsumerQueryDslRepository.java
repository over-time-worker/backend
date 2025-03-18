package com.owlexpress.consumer.infrastructure.repository;

import com.owlexpress.consumer.domain.entity.Consumer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface ConsumerQueryDslRepository {
    Page<Consumer> searchConsumer(
            String sort,
            String q,
            String orderBy,
            PageRequest pageRequest
    );
}
