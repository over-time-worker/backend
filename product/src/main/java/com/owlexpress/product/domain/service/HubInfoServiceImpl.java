package com.owlexpress.product.domain.service;

import com.owlexpress.product.application.ProductUsecaseImpl;
import com.owlexpress.product.common.dto.response.PassportDto;
import com.owlexpress.product.common.exceptions.ProductException;
import com.owlexpress.product.common.helper.PassportHelper;
import com.owlexpress.product.domain.entity.HubInfo;
import com.owlexpress.product.domain.repository.HubInfoRepository;
import com.owlexpress.product.presentation.ProductUsecase;
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
    private final ProductUsecase productUsecase;
    private final PassportHelper passportHelper;

    @Override
    @Transactional
    public HubInfo create(CreateHubInfoRequestDto createHubInfoRequestDto,
                          String passport
    ) {
        PassportDto passportDto = passportHelper.getPassportDto(passport);
        HubInfo hubInfo = CreateHubInfoRequestDto.toEntity(createHubInfoRequestDto);
        hubInfo.updateCreateData(passportDto.getUserId());

        return hubInfoRepository.save(hubInfo);
    }

    @Override
    @Transactional
    public void update(
            UUID hubInfoId,
            UpdateHubInfoRequestDto updateHubInfoRequestDto,
            String passport
    )
    {
        PassportDto passportDto = passportHelper.getPassportDto(passport);
        HubInfo hubInfo = getHubInfo(hubInfoId);
        hubInfo.setHubProductQuantity(updateHubInfoRequestDto.getHubProductQuantity());

        hubInfo.updateModifiedData(passportDto.getUserId());
    }

    @Override
    @Transactional
    public void delete(UUID hubInfoId,
                       String passport
    ) {
        PassportDto passportDto = passportHelper.getPassportDto(passport);
        HubInfo hubInfo = getHubInfo(hubInfoId);

        hubInfo.updateModifiedData(passportDto.getUserId());
        hubInfo.softDeleteData(passportDto.getUserId());
        productUsecase.disConnect(hubInfo);
    }

    private HubInfo getHubInfo(UUID hubInfoId) {
        return hubInfoRepository.findById(hubInfoId).orElseThrow(
                () -> new ProductException.HubInfoNotFoundException("HubInfo 정보를 찾을 수 없습니다.")
        );
    }
}
