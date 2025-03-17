package com.owlexpress.product.infrastructure.repository;

import com.owlexpress.product.domain.entity.HubInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface HubInfoJpaRepository extends JpaRepository<HubInfo, UUID> {
}
