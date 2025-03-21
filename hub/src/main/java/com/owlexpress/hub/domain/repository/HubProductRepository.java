package com.owlexpress.hub.domain.repository;

import com.owlexpress.hub.domain.entity.HubProduct;

import java.util.Optional;
import java.util.UUID;

public interface HubProductRepository {
    Optional<HubProduct> findByProductId(UUID productId);
}
