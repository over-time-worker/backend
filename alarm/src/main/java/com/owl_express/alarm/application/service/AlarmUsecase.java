package com.owl_express.alarm.application.service;

import com.owl_express.alarm.common.dto.request.AlarmCreateRequestDto;
import com.owl_express.alarm.common.dto.response.MessageCreateResponseDto;
import java.util.UUID;

public interface AlarmUsecase {
    MessageCreateResponseDto getHubDeliverMessageFromAi(AlarmCreateRequestDto requestDto, String passport);

    MessageCreateResponseDto getCompanyDeliverMessageFromAi(AlarmCreateRequestDto requestDto, String passport);

    void deleteMessageToAi(UUID aiId, String passport);

}
