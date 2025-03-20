package com.owlexpress.deliverymanager.infrastructure.repository.consumer;

import com.owlexpress.deliverymanager.domain.entity.ConsumerDeliveryManager;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ConsumerDeliveryManagerJpaRepository extends JpaRepository<ConsumerDeliveryManager, UUID> {
    boolean existsByAssignNumber(Integer assignNumber);
}
