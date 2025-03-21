package com.owlexpress.hub.infrastructure.repository.hubProduct;

import com.owlexpress.hub.domain.entity.HubProduct;
import com.owlexpress.hub.domain.repository.HubProductRepository;
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
        return hubProductJpaRepository.findById(productId);
    }
}
