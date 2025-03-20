package com.owlexpress.deliverymanager.infrastructure.repository.consumer;

import com.owlexpress.deliverymanager.domain.entity.ConsumerDeliveryManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface ConsumerDeliveryManagerQueryDslRepository {
    Page<ConsumerDeliveryManager> search(
            String sort,
            String q,
            String orderBy,
            PageRequest pageRequest
    );
}
