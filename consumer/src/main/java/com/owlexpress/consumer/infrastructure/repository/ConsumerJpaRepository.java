package com.owlexpress.consumer.infrastructure.repository;

import com.owlexpress.consumer.domain.entity.Consumer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ConsumerJpaRepository extends JpaRepository<Consumer, UUID> {
    Optional<Consumer> findByCompanyName(String companyName);
}
