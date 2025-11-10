package com.owlexpress.hub.domain.repository;

import com.owlexpress.hub.domain.entity.HubProduct;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface HubProductRepository {
    Optional<HubProduct> findByProductId(UUID productId);

    HubProduct save(HubProduct hubProduct);

    void saveAll(List<HubProduct> hubProducts);

    Optional<HubProduct> findByHubProductId(UUID hubProductId);

    List<HubProduct> findAllHubProductsIn(List<UUID> productIds);

    int decreaseStock(int quantity, UUID hubProductId);
}
