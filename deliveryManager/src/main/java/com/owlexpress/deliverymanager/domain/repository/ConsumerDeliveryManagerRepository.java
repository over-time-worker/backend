package com.owlexpress.deliverymanager.domain.repository;

import com.owlexpress.deliverymanager.domain.entity.ConsumerDeliveryManager;

import java.util.Optional;
import java.util.UUID;

public interface ConsumerDeliveryManagerRepository {
    ConsumerDeliveryManager save(ConsumerDeliveryManager consumerDeliveryManager);

    Optional<ConsumerDeliveryManager> findById(UUID consumerDeliveryManagerId);

    boolean existByAssignNumber(Integer assignNumber);
}
