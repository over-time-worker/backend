package com.owlexpress.deliverymanager.domain.repository;

import com.owlexpress.deliverymanager.domain.entity.ConsumerDeliveryManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Optional;
import java.util.UUID;

public interface ConsumerDeliveryManagerRepository {
    ConsumerDeliveryManager save(ConsumerDeliveryManager consumerDeliveryManager);

    Optional<ConsumerDeliveryManager> findById(UUID consumerDeliveryManagerId);

    boolean existByAssignNumber(Integer assignNumber);

    Page<ConsumerDeliveryManager> searchProducer(
            String sort,
            String q,
            String orderBy,
            PageRequest pageRequest
    );

    Optional<ConsumerDeliveryManager> findFirstByOrderByAssignNumberDesc();

    Optional<ConsumerDeliveryManager> findFirstByHubIdAndIsAvaliableTrueOrderByAssignNumberAsc(UUID hubId);
}
