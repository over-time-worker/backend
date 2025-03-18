package com.owlexpress.hub.infrastructure.repository;

import com.owlexpress.hub.domain.entity.HubIntervalInfo;
import com.owlexpress.hub.domain.repository.HubIntervalInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class HubIntervalInfoRepositoryImpl implements HubIntervalInfoRepository {
    private final HubIntervalInfoJpaRepository hubIntervalInfoJpaRepository;

    @Override
    public HubIntervalInfo save(HubIntervalInfo intervalInfo) {
        return hubIntervalInfoJpaRepository.save(intervalInfo);
    }
}
