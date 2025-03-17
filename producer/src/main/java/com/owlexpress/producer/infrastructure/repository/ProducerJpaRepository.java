package com.owlexpress.producer.infrastructure.repository;

import com.owlexpress.producer.domain.entity.Producer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ProducerJpaRepository extends JpaRepository<Producer, UUID> {
    Optional<Producer> findByCompanyName(String companyName);
}
