package com.owlexpress.hub.infrastructure;

import com.owlexpress.hub.domain.entity.Hub;
import com.owlexpress.hub.domain.repository.HubRepository;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class HubRepositoryImpl implements HubRepository {

    private final HubJpaRepository hubJpaRepository;
    private final HubQueryRepository hubQueryRepository;

    @Override
    public Optional<Hub> findByHubId(UUID hubId) {
        return hubJpaRepository.findById(hubId);
    }

    @Override
    public void save(Hub hub) {
        hubJpaRepository.save(hub);
    }
}
