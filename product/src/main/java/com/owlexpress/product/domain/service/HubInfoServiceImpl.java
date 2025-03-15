package com.owlexpress.product.domain.service;

import com.owlexpress.product.common.exceptions.ProductException;
import com.owlexpress.product.domain.entity.HubInfo;
import com.owlexpress.product.domain.repository.HubInfoRepository;
import com.owlexpress.product.presentation.dto.request.CreateHubInfoRequestDto;
import com.owlexpress.product.presentation.dto.request.UpdateHubInfoRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class HubInfoServiceImpl implements HubInfoService {

    private final HubInfoRepository hubInfoRepository;

    @Override
    @Transactional
    public HubInfo create(CreateHubInfoRequestDto createHubInfoRequestDto) {
        HubInfo hubInfo = CreateHubInfoRequestDto.toEntity(createHubInfoRequestDto);
        hubInfo.updateCreateData(1L); //TODO:: AuditAware 적용 후 삭제

        return hubInfoRepository.save(hubInfo);
    }

    @Override
    @Transactional
    public void update(
            UUID hubInfoId,
            UpdateHubInfoRequestDto updateHubInfoRequestDto
    )
    {
        HubInfo hubInfo = getHubInfo(hubInfoId);
        hubInfo.setHubProductQuantity(updateHubInfoRequestDto.getHubProductQuantity());

        hubInfo.updateModifiedData(1L); //TODO:: AuditAware 적용 후 삭제
    }

    private HubInfo getHubInfo(UUID hubInfoId) {
        return hubInfoRepository.findById(hubInfoId).orElseThrow(
                () -> new ProductException.HubInfoNotFoundException("HubInfo 정보를 찾을 수 없습니다.")
        );
    }
}
