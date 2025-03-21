package com.owlexpress.hub.domain.service;

import com.owlexpress.hub.presentation.dto.request.HubCreateRequestDto;
import com.owlexpress.hub.presentation.dto.request.HubUpdateRequestDto;
import com.owlexpress.hub.presentation.dto.response.HubFindResponseDto;
import com.owlexpress.hub.presentation.dto.response.HubSearchResponseDto;
import org.springframework.data.web.PagedModel;

import java.util.UUID;

public interface HubService {

    /*
    허브
     */
    HubFindResponseDto find(UUID hubId);

    void create(HubCreateRequestDto requestDto,
                String passport
    );

    void update(
            HubUpdateRequestDto requestDto,
            String passport
    );

    PagedModel<HubSearchResponseDto> searchHub(int page, int size, String sort, String q,
                                               String orderBy);

    void delete(UUID hubId,
                String passport
    );
}
