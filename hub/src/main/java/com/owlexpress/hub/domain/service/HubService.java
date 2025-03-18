package com.owlexpress.hub.domain.service;

import com.owlexpress.hub.presentation.dto.request.HubCreateRequestDto;
import com.owlexpress.hub.presentation.dto.request.HubProductUpdateRequestDto;
import com.owlexpress.hub.presentation.dto.response.HubFindResponseDto;
import com.owlexpress.hub.presentation.dto.request.HubUpdateRequestDto;
import com.owlexpress.hub.presentation.dto.response.HubProductFindResponseDto;
import com.owlexpress.hub.presentation.dto.response.HubProductSearchResponseDto;
import com.owlexpress.hub.presentation.dto.response.HubSearchResponseDto;
import java.util.UUID;
import org.springframework.data.web.PagedModel;

public interface HubService {
    /*
    허브
     */
    HubFindResponseDto find(UUID hubId);

    void create(HubCreateRequestDto requestDto);

    void update(HubUpdateRequestDto requestDto);

    PagedModel<HubSearchResponseDto> searchHub(int page, int size, String sort, String q, String orderBy);

    /*
     허브 상품
     */
    PagedModel<HubProductSearchResponseDto> searchHubProduct(int page, int size, String sort, String q, String orderBy);

    void update(HubProductUpdateRequestDto requestDto);

    HubProductFindResponseDto findHubProduct(UUID hubProductId);
}
