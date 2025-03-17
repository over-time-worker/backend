package com.owlexpress.hub.domain.service;

import com.owlexpress.hub.presentation.dto.request.HubCreateRequestDto;
import com.owlexpress.hub.presentation.dto.response.HubFindResponseDto;
import com.owlexpress.hub.presentation.dto.request.HubUpdateRequestDto;
import com.owlexpress.hub.presentation.dto.response.HubSearchResponseDto;
import java.util.UUID;
import org.springframework.data.web.PagedModel;

public interface HubService {

    public HubFindResponseDto find(UUID hubID);

    public void create(HubCreateRequestDto requestDto);

    public void update(HubUpdateRequestDto requestDto);

    PagedModel<HubSearchResponseDto> search(int page, int size, String sort, String q, String orderBy);
}
