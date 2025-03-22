package com.owlexpress.consumer.application.usecase;

import com.owlexpress.consumer.common.dto.request.CreateConsumerRequestDto;
import com.owlexpress.consumer.common.dto.request.UpdateConsumerRequestDto;
import com.owlexpress.consumer.common.dto.response.GetUserInfoResponseDto;
import com.owlexpress.consumer.common.dto.response.HubFindResponseDto;
import com.owlexpress.consumer.common.dto.response.PassportDto;
import com.owlexpress.consumer.common.exceptions.ConsumerException;
import com.owlexpress.consumer.common.exceptions.ExceptionMessage;
import com.owlexpress.consumer.common.helper.PassportHelper;
import com.owlexpress.consumer.common.util.ConsumerHelper;
import com.owlexpress.consumer.common.util.GeoUtil;
import com.owlexpress.consumer.domain.entity.Consumer;
import com.owlexpress.consumer.domain.repository.ConsumerRepository;
import com.owlexpress.consumer.infrastructure.feignClient.CartClient;
import com.owlexpress.consumer.infrastructure.feignClient.DeliveryClient;
import com.owlexpress.consumer.infrastructure.feignClient.HubFeignClient;
import com.owlexpress.consumer.infrastructure.feignClient.UserFeignClient;
import feign.FeignException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static com.owlexpress.consumer.common.exceptions.ExceptionMessage.DELIVERY_EXIST;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConsumerUsecase {
    private final ConsumerRepository consumerRepository;
    private final UserFeignClient userFeignClient;
    private final HubFeignClient hubFeignClient;
    private final ConsumerHelper consumerHelper;
    private final PassportHelper passportHelper;
    private final DeliveryClient deliveryClient;
    private final CartClient cartClient;

    @Transactional
    public void create(CreateConsumerRequestDto consumerRequestDto,
                       String passport
    ) {
        PassportDto passportDto = passportHelper.getPassportDto(passport);
        isDuplicatedConsumerName(consumerRequestDto);

        //  1. 유저 정보 조회
        GetUserInfoResponseDto getUserInfoResponseDto = userFeignClient.get(passport)
                                                     .getData();
        //  2.관리 허브 ID로 허브 조회
        HubFindResponseDto hubFindResponseDto = hubFeignClient.find(consumerRequestDto.getHubId())
                                                .getData();

        Consumer consumer = toEntity(
                consumerRequestDto,
                hubFindResponseDto,
                getUserInfoResponseDto
        );

        consumer.updateCreateData(passportDto.getUserId());
        consumerRepository.save(consumer);

    }

    @Transactional
    public void update(
            UUID consumerId,
            @Valid UpdateConsumerRequestDto updateConsumerRequestDto,
            String passport
    ) {
        PassportDto passportDto = passportHelper.getPassportDto(passport);
        Consumer consumer = consumerHelper.getConsumer(consumerId);

        //feignClient로 수정할 데이터 조회
        updateConsumerUserInfoIfNotNull(
                updateConsumerRequestDto,
                consumer,
                passport
        );

        consumer.setCompanyAddress(updateConsumerRequestDto.getCompanyAddress());
        consumer.setCompanyName(updateConsumerRequestDto.getCompanyName());
        consumer.setCompanyType(updateConsumerRequestDto.getCompanyType());

        consumer.updateModifiedData(passportDto.getUserId());
    }

    @Transactional
    public void delete(UUID consumerId,
                       String passport
    ) throws ConsumerException.ConsumerDeliveryException {
        Consumer consumer = consumerHelper.getConsumer(consumerId);
        PassportDto passportDto = passportHelper.getPassportDto(passport);

        //배송쪽 수령업체 조회 후 데이터가 현재 이후인 경우 삭제 불가 예외 처리
        if (deliveryClient.findByConsumer(consumerId)
                          .getData()) {
            throw new ConsumerException.ConsumerDeliveryException(DELIVERY_EXIST);
        }
        try{
            cartClient.deleteCart(passport,consumerId);
        }catch (FeignException e){
            log.error("error Message ={}",e.getMessage());
            consumer.softDeleteData(passportDto.getUserId());
        }

        consumer.softDeleteData(passportDto.getUserId());
    }

    private void updateHubInfoIfNotNull(
            UpdateConsumerRequestDto updateConsumerRequestDto,
            Consumer consumer
    ) {
        if(updateConsumerRequestDto.getHubId() != null) {
            try{
                HubFindResponseDto hubFindResponseDto = hubFeignClient.find(updateConsumerRequestDto.getHubId())
                                                                      .getData();

                consumer.setHubId(hubFindResponseDto.getHubId());
            }catch (Exception e){
                throw new ConsumerException.FeignClientException("예기치 않은 문제 발생 : " + e);
            }
        }
    }

    private void updateConsumerUserInfoIfNotNull(
            UpdateConsumerRequestDto updateConsumerRequestDto,
            Consumer consumer,
            String passport
    ) {
        if (updateConsumerRequestDto.getUserId() != null) {
            try{
                GetUserInfoResponseDto getUserInfoResponseDto = userFeignClient.get(passport).getData();
                consumer.setUserId(getUserInfoResponseDto.getUserId());
                consumer.setUserName(getUserInfoResponseDto.getUsername());
                consumer.setUserPhoneNumber(getUserInfoResponseDto.getPhoneNumber());
            }catch (Exception e){
                throw new ConsumerException.FeignClientException("예기치 않은 문제 발생 : " + e);
            }
        }
    }

    private void isDuplicatedConsumerName(CreateConsumerRequestDto consumerRequestDto) {
        consumerRepository.findByCompanyName(consumerRequestDto.getCompanyName())
                          .ifPresent(consumer -> {
                              throw new ConsumerException.ConsumerNameDuplicateExceptoin("이미 해당 업체명이 존재합니다");
                          });
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
                       .userName(getUserInfoResponseDto.getUsername())
                       .userPhoneNumber(getUserInfoResponseDto.getPhoneNumber())
                       .location(GeoUtil.createPoint(consumerRequestDto.getLongitude(), consumerRequestDto.getLatitude()))
                       .businessNumber(consumerRequestDto.getBusinessNumber())
                       .build();
    }
}
