package com.owlexpress.hub.domain.repository;

import com.owlexpress.hub.domain.entity.Hub;
import com.owlexpress.hub.domain.entity.HubProduct;
import com.owlexpress.hub.presentation.dto.response.HubProductSearchResponseDto;
import com.owlexpress.hub.presentation.dto.response.HubSearchResponseDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;

public interface HubRepository {

    Hub save(Hub hub);

    Optional<Hub> findByHubId(UUID hubId);

    PagedModel<HubSearchResponseDto> searchHub(Pageable pageable, String keyword, String orderBy, String sort);

    /*
    허브 상품
     */
    PagedModel<HubProductSearchResponseDto> searchHubProduct(Pageable pageable, String keyword, String orderBy, String sort);

    Optional<HubProduct> findByHubProductId(UUID hubProductId);

    List<Hub> findAllWithIntervals();
}
