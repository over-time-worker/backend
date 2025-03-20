package com.owlexpress.deliverymanager.domain.repository;

import com.owlexpress.deliverymanager.domain.entity.HubDeliveryManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Optional;
import java.util.UUID;

public interface HubDeliveryManagerRepository {
    HubDeliveryManager save(HubDeliveryManager hubDeliveryManager);

    Optional<HubDeliveryManager> findById(UUID hubDeliveryManagerId);

    boolean isExistAssign_number(Integer assignNumber);

    Page<HubDeliveryManager> searchProducer(
            String sort,
            String q,
            String orderBy,
            PageRequest pageRequest
    );

    Optional<HubDeliveryManager> findFirstByOrderByAssignNumberDesc();
}
