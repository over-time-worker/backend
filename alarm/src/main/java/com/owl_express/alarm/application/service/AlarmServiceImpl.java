package com.owl_express.alarm.application.service;

import com.owl_express.alarm.application.dtos.CommonDto;
import com.owl_express.alarm.application.dtos.request.AlarmCreateRequestDto;
import com.owl_express.alarm.application.dtos.request.MessageCreateRequestDto;
import com.owl_express.alarm.application.dtos.response.MessageCreateResponseDto;
import com.owl_express.alarm.application.exceptions.AlarmException.AiFeignClientException;
import com.owl_express.alarm.application.exceptions.AlarmException.OrderNotFoundException;
import com.owl_express.alarm.application.exceptions.AlarmException.SlackException;
import com.owl_express.alarm.domain.entity.Notification;
import com.owl_express.alarm.domain.entity.Notification.PlatformType;
import com.owl_express.alarm.domain.repository.AlarmRepository;
import com.owl_express.alarm.infrastructure.feignClient.AiClient;
import com.slack.api.Slack;
import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.request.chat.ChatPostMessageRequest;
import com.slack.api.methods.response.chat.ChatPostMessageResponse;
import com.slack.api.webhook.Payload;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AlarmServiceImpl implements AlarmService {

    private final AlarmRepository alarmRepository;
    private final AiClient aiClient;

    @Value("${slack.token}")
    private String slackBotToken;

    @Override
    public void createAlarmForHubDeliver(AlarmCreateRequestDto requestDto) {
        // TODO : order service 생긴 후 feign client 통신으로 정보 얻어오기 or 배송쪽에서 product 정보 함께 넘기기(메세지큐 방식이면 괜찮을듯)
        String productInfo = "생쭈꾸미 10마리, 냉동 쭈꾸미 1팩";

        // TODO : ai 메세지 요청 feign client 통신 test
        // MessageCreateResponseDto messageCreateResponseDto = getMessageFromAi(requestDto, productInfo);
        //MockData
        MessageCreateResponseDto messageCreateResponseDto
                = MessageCreateResponseDto.builder()
                .aiId(UUID.randomUUID())
                .message("자 슬랙 메세지 전송 테스트 해보자 !")
                .build();

        //slack 전송
        PlatformType platformType = PlatformType.getType(requestDto.getPlatformName());

        if(platformType.equals(PlatformType.SLACK)) {
            sendMessage(messageCreateResponseDto.getMessage(), requestDto.getDeliverPlatformId());
        }

        //alarm 생성
        Notification notification = Notification.builder()
                .aiId(messageCreateResponseDto.getAiId())
                .userId(requestDto.getUserId())
                .userPlatformId(requestDto.getDeliverPlatformId())
                .platformType(platformType)
                .message(messageCreateResponseDto.getMessage())
                .aiId(messageCreateResponseDto.getAiId())
                .sendAt(LocalDateTime.now())
                .isSend(true)
                .build();

        // TODO : UserId 넣어주기
        notification.createdEntity(1L);

        alarmRepository.save(notification);

    }

    public MessageCreateResponseDto getMessageFromAi(AlarmCreateRequestDto requestDto, String productInfo) {
        MessageCreateResponseDto messageCreateResponseDto;

        try{

            CommonDto<MessageCreateResponseDto> responseEntity
                    = aiClient.createMessagesForHubDeliver(MessageCreateRequestDto.AlarmDtoToMessageDto(requestDto,productInfo));

            messageCreateResponseDto = responseEntity.getData();
          
        } catch (RuntimeException e) {
            log.error(e.getMessage());
            throw new AiFeignClientException("잠시 후에 다시 시도해주세요.");
        }

        if(messageCreateResponseDto == null) {
            log.error("messageCreateResponseDto is null");
            throw new AiFeignClientException("메세지 생성에 실패하였습니다.");
        }

        return messageCreateResponseDto;
    }

    public void sendMessage(String message, String channelId) {
        try {
            MethodsClient client = Slack.getInstance().methods(slackBotToken);

            ChatPostMessageRequest request = ChatPostMessageRequest.builder()
                    .channel(channelId)
                    .text(message)
                    .unfurlLinks(true)
                    .unfurlMedia(true)
                    .build();

            client.chatPostMessage(request);

        } catch (IOException | SlackApiException e) {
            throw new SlackException(e.getMessage());
        }
    }

}
