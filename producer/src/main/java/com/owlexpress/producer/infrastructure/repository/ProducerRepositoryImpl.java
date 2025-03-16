package com.owlexpress.producer.infrastructure.repository;

import com.owlexpress.producer.domain.entity.Producer;
import com.owlexpress.producer.domain.repository.ProducerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ProducerRepositoryImpl implements ProducerRepository {
    private final ProducerJpaRepository producerJpaRepository;

    @Override
    public Producer save(Producer producer) {
        return producerJpaRepository.save(producer);
    }

    @Override
    public Optional<Producer> findByCompanyName(String companyName) {
        return producerJpaRepository.findByCompanyName(companyName);
    }
}
