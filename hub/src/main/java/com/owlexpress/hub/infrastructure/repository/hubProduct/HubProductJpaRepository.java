package com.owlexpress.hub.infrastructure.repository.hubProduct;

import com.owlexpress.hub.domain.entity.HubProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

public interface HubProductJpaRepository extends JpaRepository<HubProduct, UUID> {
}
