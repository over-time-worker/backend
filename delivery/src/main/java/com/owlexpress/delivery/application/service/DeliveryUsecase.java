package com.owlexpress.delivery.application.service;

import static com.owlexpress.delivery.common.exception.ExceptionMessage.DELIVERY_MANAGER_ASSIGN_FAIL_MESSAGE;

import com.owlexpress.delivery.application.dtos.CommonDto;
import com.owlexpress.delivery.application.dtos.request.DeliveryManagerRequestDto;
import com.owlexpress.delivery.application.dtos.response.AlarmCreateResponseDto;
import com.owlexpress.delivery.application.exceptions.DeliveryException;
import com.owlexpress.delivery.application.exceptions.DeliveryException.DeliveryManagerFeignClientException;
import com.owlexpress.delivery.infrastructure.feignClient.DeliveryManagerClient;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeliveryUsecase {

    private final DeliveryManagerClient deliveryManagerClient;

    public AlarmCreateResponseDto assignHubDeliverFromDeliveryManager(DeliveryManagerRequestDto deliveryManagerRequestDto) {
        AlarmCreateResponseDto messageCreateResponseDto;

        CommonDto<AlarmCreateResponseDto> responseEntity
                = deliveryManagerClient.assignHub(deliveryManagerRequestDto);

        messageCreateResponseDto = responseEntity.getData();

        if(messageCreateResponseDto == null) {
            throw new DeliveryManagerFeignClientException(DELIVERY_MANAGER_ASSIGN_FAIL_MESSAGE);
        }

        return messageCreateResponseDto;
    }

    public void returnHubDeliverToDeliveryManager(UUID hubDeliverId) {

        deliveryManagerClient.returnHub(hubDeliverId);

    }

    public AlarmCreateResponseDto assignCompanyDeliverFromDeliveryManager(DeliveryManagerRequestDto deliveryManagerRequestDto) {
        AlarmCreateResponseDto messageCreateResponseDto;

        CommonDto<AlarmCreateResponseDto> responseEntity
                = deliveryManagerClient.assignHub(deliveryManagerRequestDto);

        messageCreateResponseDto = responseEntity.getData();

        if(messageCreateResponseDto == null) {
            throw new DeliveryManagerFeignClientException(DELIVERY_MANAGER_ASSIGN_FAIL_MESSAGE);
        }

        return messageCreateResponseDto;
    }

    public void returnCompanyDeliverToDeliveryManager(UUID hubDeliverId) {

        deliveryManagerClient.returnCompany(hubDeliverId);

    }
}
