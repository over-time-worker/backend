package com.owlexpress.delivery.application.service;

import com.owlexpress.delivery.common.dto.request.DeliveryManagerRequestDto;
import com.owlexpress.delivery.common.dto.request.HubDeliverFallbackMessageCreateRequestDto;
import com.owlexpress.delivery.common.dto.response.AlarmCreateResponseDto;
import java.util.UUID;

public interface DeliveryUsecase {

    AlarmCreateResponseDto assignHubDeliverFromDeliveryManager(
            DeliveryManagerRequestDto deliveryManagerRequestDto,
            String passport
    );

    void returnHubDeliverToDeliveryManager(UUID hubDeliverId);

    AlarmCreateResponseDto assignCompanyDeliverFromDeliveryManager(
            DeliveryManagerRequestDto deliveryManagerRequestDto,
            String passport
    );

    void returnCompanyDeliverToDeliveryManager(UUID hubDeliverId, String passport);

    void DeleteCompanyDeliverMessageToAlarm(
            String channelId,
            String messageId,
            String passport
    );

    void sendFallbackHubDeliverMessageToAlarm(
            HubDeliverFallbackMessageCreateRequestDto requestDto,
            String passport
    );
}
