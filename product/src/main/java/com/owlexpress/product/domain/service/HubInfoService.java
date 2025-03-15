package com.owlexpress.product.domain.service;

import com.owlexpress.product.domain.entity.HubInfo;
import com.owlexpress.product.presentation.dto.request.CreateHubInfoRequestDto;

public interface HubInfoService {
    HubInfo create(CreateHubInfoRequestDto createHubInfoRequestDto);
}
