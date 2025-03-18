package com.owlexpress.consumer.domain.repository;

import com.owlexpress.consumer.domain.entity.Consumer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Optional;
import java.util.UUID;

public interface ConsumerRepository {
    Optional<Consumer> findByCompanyName(String companyName);

    Consumer save(Consumer consumer);

    Optional<Consumer> findById(UUID consumerId);

    Page<Consumer> searchProducer(
            String sort,
            String q,
            String orderBy,
            PageRequest pageRequest
    );
}
