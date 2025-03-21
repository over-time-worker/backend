package com.owlexpress.hub.infrastructure.repository.hubProduct;

import com.owlexpress.hub.domain.entity.HubProduct;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface HubProductJpaRepository extends JpaRepository<HubProduct, UUID> {
    Optional<HubProduct> findByProductId(UUID productId);
}
