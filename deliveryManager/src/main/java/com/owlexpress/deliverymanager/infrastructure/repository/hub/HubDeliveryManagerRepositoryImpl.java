package com.owlexpress.deliverymanager.infrastructure.repository.hub;

import com.owlexpress.deliverymanager.domain.entity.HubDeliveryManager;
import com.owlexpress.deliverymanager.domain.repository.HubDeliveryManagerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class HubDeliveryManagerRepositoryImpl implements HubDeliveryManagerRepository {
    private final HubDeliveryManagerJpaRepository hubDeliveryManagerJpaRepository;

    @Override
    public HubDeliveryManager save(HubDeliveryManager hubDeliveryManager) {
        return hubDeliveryManagerJpaRepository.save(hubDeliveryManager);
    }

    @Override
    public Optional<HubDeliveryManager> findById(UUID hubDeliveryManagerId) {
        return hubDeliveryManagerJpaRepository.findById(hubDeliveryManagerId);
    }

    @Override
    public boolean isExistAssign_number(Integer assignNumber) {
        return hubDeliveryManagerJpaRepository.existsByAssignNumber(assignNumber);
    }
}
