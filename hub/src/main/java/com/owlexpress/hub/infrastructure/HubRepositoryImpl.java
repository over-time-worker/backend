package com.owlexpress.hub.infrastructure;

import com.owlexpress.hub.domain.entity.Hub;
import com.owlexpress.hub.domain.repository.HubRepository;
import com.owlexpress.hub.presentation.dto.response.HubSearchResponseDto;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
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
    public PagedModel<HubSearchResponseDto> searchHub(Pageable pageable, String keyword,
            String orderBy, String sort) {
        Page<Hub> hubs = hubQueryRepository.searchHub(pageable, keyword, orderBy, sort);
        Page<HubSearchResponseDto> results = hubs.map(HubSearchResponseDto::fromEntity);
        return new PagedModel<>(results);
    }

    @Override
    public void save(Hub hub) {
        hubJpaRepository.save(hub);
    }

}
