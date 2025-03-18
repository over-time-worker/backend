package com.owlexpress.hub.domain.repository;

import com.owlexpress.hub.domain.entity.Hub;
import com.owlexpress.hub.presentation.dto.response.HubProductSearchResponseDto;
import com.owlexpress.hub.presentation.dto.response.HubSearchResponseDto;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;

public interface HubRepository {

    public Hub save(Hub hub);

    public Optional<Hub> findByHubId(UUID hubId);

    public PagedModel<HubSearchResponseDto> searchHub(Pageable pageable, String keyword, String orderBy, String sort);
    public PagedModel<HubProductSearchResponseDto> searchHubProduct(Pageable pageable, String keyword, String orderBy, String sort);
}
