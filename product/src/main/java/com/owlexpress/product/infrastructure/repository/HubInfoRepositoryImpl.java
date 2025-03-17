package com.owlexpress.product.infrastructure.repository;

import com.owlexpress.product.domain.entity.HubInfo;
import com.owlexpress.product.domain.repository.HubInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Repository
public class HubInfoRepositoryImpl implements HubInfoRepository {
    private final HubInfoJpaRepository jpaRepository;

    @Override
    public HubInfo save(HubInfo hubInfo) {
        return jpaRepository.save(hubInfo);
    }

    @Override
    public Optional<HubInfo> findById(UUID hubInfoId) {
        return jpaRepository.findById(hubInfoId);
    }
}
