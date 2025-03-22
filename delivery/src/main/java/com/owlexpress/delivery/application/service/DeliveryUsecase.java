package com.owlexpress.delivery.application.service;

import static com.owlexpress.delivery.common.exception.ExceptionMessage.DELIVERY_MANAGER_ASSIGN_FAIL_MESSAGE;

import com.owlexpress.delivery.application.dtos.CommonDto;
import com.owlexpress.delivery.application.dtos.request.DeliveryManagerRequestDto;
import com.owlexpress.delivery.application.dtos.request.HubDeliverFallbackMessageCreateRequestDto;
import com.owlexpress.delivery.application.dtos.response.AlarmCreateResponseDto;
import com.owlexpress.delivery.application.exceptions.DeliveryException;
import com.owlexpress.delivery.application.exceptions.DeliveryException.DeliveryManagerFeignClientException;
import com.owlexpress.delivery.infrastructure.feignClient.AlarmClient;
import com.owlexpress.delivery.infrastructure.feignClient.DeliveryManagerClient;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@Service
@RequiredArgsConstructor
public class DeliveryUsecase {

    private final DeliveryManagerClient deliveryManagerClient;
    private final AlarmClient alarmClient;

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

    public void returnHubDeliverToDeliveryManager(UUID hubDeliverId) {

        deliveryManagerClient.returnHub(hubDeliverId);

    }

    public AlarmCreateResponseDto assignCompanyDeliverFromDeliveryManager(
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

    public void returnCompanyDeliverToDeliveryManager(UUID hubDeliverId) {
        deliveryManagerClient.returnCompany(hubDeliverId);
    }

    public void DeleteCompanyDeliverMessageToAlarm(
            String channelId,
            String messageId,
            String passport
    ) {
        alarmClient.companyDelete(channelId, messageId, passport);

    }

    public void sendFallbackHubDeliverMessageToAlarm(
            HubDeliverFallbackMessageCreateRequestDto requestDto,
            String passport
    ) {

        alarmClient.hubFallback(requestDto, passport);

    }
}
