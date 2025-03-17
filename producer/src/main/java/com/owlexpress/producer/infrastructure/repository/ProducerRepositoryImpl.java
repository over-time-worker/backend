package com.owlexpress.producer.infrastructure.repository;

import com.owlexpress.producer.domain.entity.Producer;
import com.owlexpress.producer.domain.repository.ProducerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class ProducerRepositoryImpl implements ProducerRepository {
    private final ProducerJpaRepository producerJpaRepository;
    private final ProducerQueryDslRepository producerQueryDslRepository;

    @Override
    public Producer save(Producer producer) {
        return producerJpaRepository.save(producer);
    }

    @Override
    public Optional<Producer> findByCompanyName(String companyName) {
        return producerJpaRepository.findByCompanyName(companyName);
    }

    @Override
    public Optional<Producer> findById(UUID producerId) {
        return producerJpaRepository.findById(producerId);
    }

    @Override
    public Page<Producer> searchProducer(
            String sort,
            String q,
            String orderBy,
            PageRequest pageRequest
    ) {
        return producerQueryDslRepository.searchProducer(sort, q, orderBy, pageRequest);
    }
}
