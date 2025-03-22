package com.owlexpress.product.domain.service;

import com.owlexpress.product.domain.entity.HubInfo;
import com.owlexpress.product.presentation.dto.request.CreateHubInfoRequestDto;
import com.owlexpress.product.presentation.dto.request.UpdateHubInfoRequestDto;

import java.util.UUID;

public interface HubInfoService {
    HubInfo create(CreateHubInfoRequestDto createHubInfoRequestDto,
                   String passport
    );


    void update(
            UUID hubInfoId,
            UpdateHubInfoRequestDto updateHubInfoRequestDto,
            String passport
    );

    void delete(UUID hubInfoId,
                String passport
    );
}
