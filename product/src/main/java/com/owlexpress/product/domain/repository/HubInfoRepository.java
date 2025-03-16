package com.owlexpress.product.domain.repository;

import com.owlexpress.product.domain.entity.HubInfo;

import java.util.Optional;
import java.util.UUID;

public interface HubInfoRepository {
    HubInfo save(HubInfo hubInfo);

    Optional<HubInfo> findById(UUID hubInfoId);
}
