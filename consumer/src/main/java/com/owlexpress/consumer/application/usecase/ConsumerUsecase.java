package com.owlexpress.consumer.application.usecase;

import com.owlexpress.consumer.common.dto.response.HubFindResponseDto;
import com.owlexpress.consumer.common.dto.request.CreateConsumerRequestDto;
import com.owlexpress.consumer.common.dto.response.GetUserInfoResponseDto;
import com.owlexpress.consumer.common.exceptions.ConsumerException;
import com.owlexpress.consumer.common.util.GeoUtil;
import com.owlexpress.consumer.domain.entity.Consumer;
import com.owlexpress.consumer.domain.repository.ConsumerRepository;
import com.owlexpress.consumer.infrastructure.feignClient.HubFeignClient;
import com.owlexpress.consumer.infrastructure.feignClient.UserFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ConsumerUsecase {
    private final ConsumerRepository consumerRepository;
    private final UserFeignClient userFeignClient;
    private final HubFeignClient hubFeignClient;

    public void create(CreateConsumerRequestDto consumerRequestDto) {
        isDuplicatedConsumerName(consumerRequestDto);

        //  1. 유저 정보 조회
        GetUserInfoResponseDto getUserInfoResponseDto = userFeignClient.get(consumerRequestDto.getUserId())
                                                     .getData();
        //  2.관리 허브 ID로 허브 조회
        HubFindResponseDto hubFindResponseDto = hubFeignClient.find(consumerRequestDto.getHubId())
                                                .getData();

        Consumer consumer = toEntity(
                consumerRequestDto,
                hubFindResponseDto,
                getUserInfoResponseDto
        );

        consumerRepository.save(consumer);

    }

    private static Consumer toEntity(
            CreateConsumerRequestDto consumerRequestDto,
            HubFindResponseDto hubFindResponseDto,
            GetUserInfoResponseDto getUserInfoResponseDto
    ) {
        return Consumer.builder()
                .hubId(hubFindResponseDto.getHubId())
                .companyName(consumerRequestDto.getCompanyName())
                .companyType(consumerRequestDto.getCompanyType())
                .companyAddress(consumerRequestDto.getCompanyAddress())
                .userId(getUserInfoResponseDto.getUserId())
                .userName(consumerRequestDto.getUserName())
                .userPhoneNumber(consumerRequestDto.getUserPhoneNumber())
                .location(GeoUtil.createPoint(consumerRequestDto.getLatitude(), consumerRequestDto.getLongitude()))
                .businessNumber(consumerRequestDto.getBusinessNumber())
                .build();
    }

    private void isDuplicatedConsumerName(CreateConsumerRequestDto consumerRequestDto) {
        consumerRepository.findByCompanyName(consumerRequestDto.getCompanyName())
                          .ifPresent(consumer -> {
                              throw new ConsumerException.ConsumerNameDuplicateExceptoin("이미 해당 업체명이 존재합니다");
                          });
    }
}
