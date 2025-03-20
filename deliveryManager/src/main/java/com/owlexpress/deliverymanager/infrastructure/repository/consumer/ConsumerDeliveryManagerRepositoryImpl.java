package com.owlexpress.deliverymanager.infrastructure.repository.consumer;

import com.owlexpress.deliverymanager.domain.entity.ConsumerDeliveryManager;
import com.owlexpress.deliverymanager.domain.repository.ConsumerDeliveryManagerRepository;
import com.owlexpress.deliverymanager.domain.repository.HubDeliveryManagerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class ConsumerDeliveryManagerRepositoryImpl implements ConsumerDeliveryManagerRepository {
    private final ConsumerDeliveryManagerJpaRepository consumerDeliveryManagerJpaRepository;

    @Override
    public ConsumerDeliveryManager save(ConsumerDeliveryManager consumerDeliveryManager) {
        return consumerDeliveryManagerJpaRepository.save(consumerDeliveryManager);
    }

    @Override
    public Optional<ConsumerDeliveryManager> findById(UUID consumerDeliveryManagerId) {
        return consumerDeliveryManagerJpaRepository.findById(consumerDeliveryManagerId);
    }

    @Override
    public boolean existByAssignNumber(Integer assignNumber) {
        return consumerDeliveryManagerJpaRepository.existsByAssignNumber(assignNumber);
    }
}
