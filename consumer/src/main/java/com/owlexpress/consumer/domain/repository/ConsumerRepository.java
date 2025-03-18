package com.owlexpress.consumer.domain.repository;

import com.owlexpress.consumer.domain.entity.Consumer;

import java.util.Optional;

public interface ConsumerRepository {
    Optional<Consumer> findByCompanyName(String companyName);

    Consumer save(Consumer consumer);
}
