package com.owlexpress.hub.domain.service;

import com.owlexpress.hub.presentation.dto.HubCreateRequestDto;
import com.owlexpress.hub.presentation.dto.HubUpdateRequestDto;

public interface HubService {

    public void create(HubCreateRequestDto requestDto);

    public void update(HubUpdateRequestDto requestDto);
}
