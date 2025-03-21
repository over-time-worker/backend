package com.owl_express.alarm.application.service;

import static com.owl_express.alarm.common.exception.ExceptionMessage.MESSAGE_CREATE_FAIL_MESSAGE;

import com.owl_express.alarm.application.dtos.CommonDto;
import com.owl_express.alarm.application.dtos.request.AlarmCreateRequestDto;
import com.owl_express.alarm.application.dtos.request.CompanyDeliverMessageCreateRequestDto;
import com.owl_express.alarm.application.dtos.request.HubDeliverMessageCreateRequestDto;
import com.owl_express.alarm.application.dtos.response.MessageCreateResponseDto;
import com.owl_express.alarm.application.exceptions.AlarmException.AiFeignClientException;
import com.owl_express.alarm.infrastructure.feignClient.AiClient;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AlarmUsecase {

    private final AiClient aiClient;

    public MessageCreateResponseDto getHubDeliverMessageFromAi(AlarmCreateRequestDto requestDto, String passport) {
        MessageCreateResponseDto messageCreateResponseDto;
        HubDeliverMessageCreateRequestDto hubDeliverMessageCreateRequestDto = HubDeliverMessageCreateRequestDto.alarmDtoToMessageDto(requestDto);

            CommonDto<MessageCreateResponseDto> responseEntity
                    = aiClient.createMessagesForHubDeliver(hubDeliverMessageCreateRequestDto, passport);

            messageCreateResponseDto = responseEntity.getData();

        if(messageCreateResponseDto == null) {
            throw new AiFeignClientException(MESSAGE_CREATE_FAIL_MESSAGE);
        }

        return messageCreateResponseDto;
    }

    public MessageCreateResponseDto getCompanyDeliverMessageFromAi(AlarmCreateRequestDto requestDto, String passport) {
        MessageCreateResponseDto messageCreateResponseDto;

            CommonDto<MessageCreateResponseDto> responseEntity
                    = aiClient.createMessagesForCompanyDeliver(CompanyDeliverMessageCreateRequestDto.alarmDtoToMessageDto(requestDto), passport);

            messageCreateResponseDto = responseEntity.getData();

        if(messageCreateResponseDto == null) {
            throw new AiFeignClientException(MESSAGE_CREATE_FAIL_MESSAGE);
        }

        return messageCreateResponseDto;
    }

    public void deleteMessageToAi(UUID aiId, String passport) {
        aiClient.delete(aiId, passport);
    }

}
