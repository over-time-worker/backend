package com.owlexpress.hub.domain.service;

import com.owlexpress.hub.presentation.dto.request.HubCreateRequestDto;
import com.owlexpress.hub.presentation.dto.request.HubUpdateRequestDto;

public interface HubService {

    public void create(HubCreateRequestDto requestDto);

    public void update(HubUpdateRequestDto requestDto);
}
