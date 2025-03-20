package com.owlexpress.deliverymanager.infrastructure.repository.hub;

import com.owlexpress.deliverymanager.domain.entity.HubDeliveryManager;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface HubDeliveryManagerJpaRepository extends JpaRepository<HubDeliveryManager, UUID> {
    boolean existsByAssignNumber(Integer assignNumber);
}
