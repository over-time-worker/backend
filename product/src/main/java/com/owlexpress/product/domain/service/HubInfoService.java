package com.owlexpress.product.domain.service;

import com.owlexpress.product.domain.entity.HubInfo;
import com.owlexpress.product.presentation.dto.request.CreateHubInfoRequestDto;
import com.owlexpress.product.presentation.dto.request.UpdateHubInfoRequestDto;
import jakarta.validation.Valid;

import java.util.UUID;

public interface HubInfoService {
    HubInfo create(CreateHubInfoRequestDto createHubInfoRequestDto);


    void update(
            UUID hubInfoId,
            @Valid UpdateHubInfoRequestDto updateHubInfoRequestDto
    );

    void delete(UUID hubInfoId);
}
