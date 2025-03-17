package com.owlexpress.hub.domain.service;

import com.owlexpress.hub.presentation.dto.request.HubCreateRequestDto;
import com.owlexpress.hub.presentation.dto.response.HubFindResponseDto;
import com.owlexpress.hub.presentation.dto.request.HubUpdateRequestDto;
import com.owlexpress.hub.presentation.dto.response.HubSearchResponseDto;
import java.util.UUID;
import org.springframework.data.web.PagedModel;

public interface HubService {

    HubFindResponseDto find(UUID hubId);

    void create(HubCreateRequestDto requestDto);

    void update(HubUpdateRequestDto requestDto);

    PagedModel<HubSearchResponseDto> search(int page, int size, String sort, String q, String orderBy);
}
