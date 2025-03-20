package com.owlexpress.deliverymanager.infrastructure.repository.hub;

import com.owlexpress.deliverymanager.domain.entity.HubDeliveryManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface HubDeliveryManagerQueryDslRepository {
    Page<HubDeliveryManager> searchProducer(
            String sort,
            String q,
            String orderBy,
            PageRequest pageRequest
    );
}
