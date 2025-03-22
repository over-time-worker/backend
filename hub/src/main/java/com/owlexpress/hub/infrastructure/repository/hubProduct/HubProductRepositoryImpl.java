package com.owlexpress.hub.infrastructure.repository.hubProduct;

import com.owlexpress.hub.domain.entity.HubProduct;
import com.owlexpress.hub.domain.repository.HubProductRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class HubProductRepositoryImpl implements HubProductRepository {

    private final HubProductJpaRepository hubProductJpaRepository;

    @Override
    public Optional<HubProduct> findByProductId(UUID productId) {
        return hubProductJpaRepository.findByProductId(productId);
    }

    @Override
    public HubProduct save(HubProduct hubProduct) {
        return hubProductJpaRepository.save(hubProduct);
    }

    @Override
    public Optional<HubProduct> findByHubProductId(UUID hubProductId) {
        return hubProductJpaRepository.findById(hubProductId);
    }

    @Override
    public List<HubProduct> findAllHubProductsIn(List<UUID> productIds) {
        return hubProductJpaRepository.findAllByHubProductIdIn(productIds);
    }
}
