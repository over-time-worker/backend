package com.owlexpress.consumer.infrastructure.repository;

import com.owlexpress.consumer.domain.entity.Consumer;
import com.owlexpress.consumer.domain.repository.ConsumerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class ConsumerRepositoryImpl implements ConsumerRepository {
    private final ConsumerJpaRepository consumerJpaRepository;
    private final ConsumerQueryDslRepository consumerQueryDslRepository;

    @Override
    public Optional<Consumer> findByCompanyName(String companyName) {
        return consumerJpaRepository.findByCompanyName(companyName);
    }

    @Override
    public Consumer save(Consumer consumer) {
        return consumerJpaRepository.save(consumer);
    }

    @Override
    public Optional<Consumer> findById(UUID consumerId) {
        return consumerJpaRepository.findById(consumerId);
    }

}
