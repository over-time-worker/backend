package com.owlexpress.deliverymanager.infrastructure.repository.hub;

import com.owlexpress.deliverymanager.domain.entity.HubDeliveryManager;
import com.owlexpress.deliverymanager.domain.repository.HubDeliveryManagerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class HubDeliveryManagerRepositoryImpl implements HubDeliveryManagerRepository {
    private final HubDeliveryManagerJpaRepository hubDeliveryManagerJpaRepository;
    private final HubDeliveryManagerQueryDslRepository hubDeliveryManagerQueryDslRepository;

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

    @Override
    public Page<HubDeliveryManager> searchProducer(
            String sort,
            String q,
            String orderBy,
            PageRequest pageRequest
    ) {
        return hubDeliveryManagerQueryDslRepository.searchProducer(sort, q, orderBy, pageRequest);
    }
}
