package com.owlexpress.product.infrastructure.repository;

import com.owlexpress.product.domain.entity.HubInfo;
import com.owlexpress.product.domain.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface HubInfoJpaRepository extends JpaRepository<HubInfo, UUID> {
}
