package com.owlexpress.product.domain.service;

import com.owlexpress.product.domain.entity.HubInfo;
import com.owlexpress.product.domain.repository.HubInfoRepository;
import com.owlexpress.product.presentation.dto.request.CreateHubInfoRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class HubInfoServiceImpl implements HubInfoService {

    private final HubInfoRepository hubInfoRepository;

    @Override
    @Transactional
    public HubInfo create(CreateHubInfoRequestDto createHubInfoRequestDto) {
        HubInfo hubInfo = CreateHubInfoRequestDto.toEntity(createHubInfoRequestDto);
        hubInfo.updateCreateData(1L);

        return hubInfoRepository.save(hubInfo);
    }
}
