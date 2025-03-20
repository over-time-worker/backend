package com.owlexpress.deliverymanager.infrastructure.repository.consumer;

import com.owlexpress.deliverymanager.domain.entity.ConsumerDeliveryManager;
import com.owlexpress.deliverymanager.domain.repository.ConsumerDeliveryManagerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class ConsumerDeliveryManagerRepositoryImpl implements ConsumerDeliveryManagerRepository {
    private final ConsumerDeliveryManagerJpaRepository consumerDeliveryManagerJpaRepository;
    private final ConsumerDeliveryManagerQueryDslRepository consumerDeliveryManagerQueryDslRepository;

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

    @Override
    public Page<ConsumerDeliveryManager> searchProducer(
            String sort,
            String q,
            String orderBy,
            PageRequest pageRequest
    ) {
        return consumerDeliveryManagerQueryDslRepository.search(sort,q,orderBy,pageRequest);
    }
}
