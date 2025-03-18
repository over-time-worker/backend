package com.owlexpress.hub.infrastructure.repository;

import com.owlexpress.hub.domain.entity.HubIntervalInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface HubIntervalInfoJpaRepository extends JpaRepository<HubIntervalInfo, UUID> {
}
