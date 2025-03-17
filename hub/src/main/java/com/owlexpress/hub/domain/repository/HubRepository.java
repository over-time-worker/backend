package com.owlexpress.hub.domain.repository;

import com.owlexpress.hub.domain.entity.Hub;
import java.util.Optional;
import java.util.UUID;

public interface HubRepository {

    public void save(Hub hub);

    public Optional<Hub> findByHubId(UUID hubId);
}
