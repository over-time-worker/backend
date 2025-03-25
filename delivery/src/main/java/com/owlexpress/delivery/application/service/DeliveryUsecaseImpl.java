package com.owlexpress.delivery.application.service;

import static com.owlexpress.delivery.common.exception.ExceptionMessage.DELIVERY_MANAGER_ASSIGN_FAIL_MESSAGE;


import com.owlexpress.delivery.application.exceptions.DeliveryException.DeliveryManagerFeignClientException;
import com.owlexpress.delivery.common.dto.CommonDto;
import com.owlexpress.delivery.common.dto.request.DeliveryManagerRequestDto;
import com.owlexpress.delivery.common.dto.request.HubDeliverFallbackMessageCreateRequestDto;
import com.owlexpress.delivery.common.dto.response.AlarmCreateResponseDto;
import com.owlexpress.delivery.infrastructure.feignClient.AlarmClient;
import com.owlexpress.delivery.infrastructure.feignClient.DeliveryManagerClient;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeliveryUsecaseImpl implements DeliveryUsecase {

    private final DeliveryManagerClient deliveryManagerClient;
    private final AlarmClient alarmClient;

    @Override
    public AlarmCreateResponseDto assignHubDeliverFromDeliveryManager(
            DeliveryManagerRequestDto deliveryManagerRequestDto,
            String passport
    ) {
        AlarmCreateResponseDto messageCreateResponseDto;

        CommonDto<AlarmCreateResponseDto> responseEntity
                = deliveryManagerClient.assignHub(passport, deliveryManagerRequestDto);

        messageCreateResponseDto = responseEntity.getData();

        if(messageCreateResponseDto == null) {
            throw new DeliveryManagerFeignClientException(DELIVERY_MANAGER_ASSIGN_FAIL_MESSAGE);
        }

        return messageCreateResponseDto;
    }

    @Override
    public void returnHubDeliverToDeliveryManager(UUID hubDeliverId) {

        deliveryManagerClient.returnHub(hubDeliverId);

    }

    @Override
    public AlarmCreateResponseDto assignCompanyDeliverFromDeliveryManager(
            DeliveryManagerRequestDto deliveryManagerRequestDto,
            String passport
    ) {
        AlarmCreateResponseDto messageCreateResponseDto;

        CommonDto<AlarmCreateResponseDto> responseEntity
                = deliveryManagerClient.assignCompany(passport, deliveryManagerRequestDto);

        messageCreateResponseDto = responseEntity.getData();

        if(messageCreateResponseDto == null) {
            throw new DeliveryManagerFeignClientException(DELIVERY_MANAGER_ASSIGN_FAIL_MESSAGE);
        }

        return messageCreateResponseDto;
    }

    @Override
    public void returnCompanyDeliverToDeliveryManager(UUID hubDeliverId, String passport) {
        deliveryManagerClient.returnCompany(passport, hubDeliverId);
    }

    @Override
    public void DeleteCompanyDeliverMessageToAlarm(
            String channelId,
            String messageId,
            String passport
    ) {
        alarmClient.companyDelete(channelId, messageId, passport);

    }

    @Override
    public void sendFallbackHubDeliverMessageToAlarm(
            HubDeliverFallbackMessageCreateRequestDto requestDto,
            String passport
    ) {

        alarmClient.hubFallback(requestDto, passport);

    }
}
