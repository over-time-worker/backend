package com.owl_express.alarm.application.service;

import com.owl_express.alarm.application.dtos.CommonDto;
import com.owl_express.alarm.application.dtos.request.AlarmCreateRequestDto;
import com.owl_express.alarm.application.dtos.request.MessageCreateRequestDto;
import com.owl_express.alarm.application.dtos.response.AlarmCreateResponseDto;
import com.owl_express.alarm.application.dtos.response.MessageCreateResponseDto;
import com.owl_express.alarm.application.exceptions.AlarmException.AiFeignClientException;
import com.owl_express.alarm.application.exceptions.AlarmException.AlarmNotFoundException;
import com.owl_express.alarm.application.exceptions.AlarmException.SlackException;
import com.owl_express.alarm.common.util.CommonUtil;
import com.owl_express.alarm.domain.entity.Alarm;
import com.owl_express.alarm.domain.entity.Alarm.MessageType;
import com.owl_express.alarm.domain.entity.Alarm.PlatformType;
import com.owl_express.alarm.domain.repository.AlarmRepository;
import com.owl_express.alarm.infrastructure.feignClient.AiClient;
import com.slack.api.Slack;
import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.request.chat.ChatPostMessageRequest;
import com.slack.api.methods.response.chat.ChatDeleteScheduledMessageResponse;
import com.slack.api.methods.response.chat.ChatPostMessageResponse;
import com.slack.api.methods.response.chat.ChatScheduleMessageResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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
        ChatPostMessageResponse chatPostMessageResponse;

        if(platformType.equals(PlatformType.SLACK)) {
            chatPostMessageResponse = sendMessage(messageCreateResponseDto.getMessage(),
                    requestDto.getDeliverPlatformId());

            String gmtDate = chatPostMessageResponse.getHttpResponseHeaders().get("date").get(0);

            //alarm 생성
            Alarm alarm = Alarm.builder()
                    .aiId(messageCreateResponseDto.getAiId())
                    .userId(requestDto.getUserId())
                    .userPlatformId(requestDto.getDeliverPlatformId())
                    .platformType(platformType)
                    .message(messageCreateResponseDto.getMessage())
                    .aiId(messageCreateResponseDto.getAiId())
                    .sendAt(CommonUtil.gmtStringToDefaultLocalDateTime(gmtDate))
                    .messageType(MessageType.NORMAL)
                    .build();

            // TODO : UserId 넣어주기
            alarm.createdEntity(1L);
            alarmRepository.save(alarm);
        }
    }

    @Override
    public AlarmCreateResponseDto createAlarmForCompanyDeliver(AlarmCreateRequestDto requestDto) {
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
            ChatScheduleMessageResponse chatScheduleMessageResponse
                    = scheduleMessage(messageCreateResponseDto.getMessage(), requestDto.getDeliverPlatformId());

            String platformMessageId = chatScheduleMessageResponse.getScheduledMessageId();
            String gmtDate = chatScheduleMessageResponse.getHttpResponseHeaders().get("date").get(0);

            //alarm 생성
            Alarm alarm = Alarm.builder()
                    .aiId(messageCreateResponseDto.getAiId())
                    .userId(requestDto.getUserId())
                    .userPlatformId(requestDto.getDeliverPlatformId())
                    .platformType(platformType)
                    .message(messageCreateResponseDto.getMessage())
                    .aiId(messageCreateResponseDto.getAiId())
                    .sendAt(CommonUtil.gmtStringToDefaultLocalDateTime(gmtDate))
                    .messageType(MessageType.RESERVATION)
                    .messageId(platformMessageId)
                    .build();

            // TODO : UserId 넣어주기
            alarm.createdEntity(1L);
            alarmRepository.save(alarm);

            return AlarmCreateResponseDto.toDto(alarm, platformMessageId);
        }
        return AlarmCreateResponseDto.builder().build();
    }

    @Override
    public void deleteAlarm(String channelId, String messageId) {
        deleteMessage(channelId, messageId);

        Alarm alarm = alarmRepository.findByMessageId(messageId).orElseThrow(
                () -> new AlarmNotFoundException("일치 하는 알림 정보가 없습니다."));

        // TODO : UserId 넣어주기
        alarm.deleteEntity(1L);
        alarmRepository.save(alarm);
    }

    private MessageCreateResponseDto getMessageFromAi(AlarmCreateRequestDto requestDto, String productInfo) {
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

    private ChatPostMessageResponse sendMessage(String message, String channelId) {
        ChatPostMessageResponse response;
        try {
            MethodsClient client = Slack.getInstance().methods(slackBotToken);

            ChatPostMessageRequest request = ChatPostMessageRequest.builder()
                    .channel(channelId)
                    .text(message)
                    .unfurlLinks(true)
                    .unfurlMedia(true)
                    .build();

            response = client.chatPostMessage(request);

        } catch (IOException | SlackApiException e) {
            throw new SlackException(e.getMessage());
        }

        if(!(response == null) && response.isOk()) {
            return response;
        } else {
            throw new SlackException("메세지 전송에 실패했습니다.");
        }
    }

    private ChatScheduleMessageResponse scheduleMessage(String message, String channelId) {
        ChatScheduleMessageResponse response;
        ZonedDateTime koreaTime = ZonedDateTime.of(LocalDate.now(), LocalTime.of(6,0), ZoneId.of("Asia/Seoul"));
        ZonedDateTime serverTime = koreaTime.withZoneSameInstant(ZoneId.systemDefault());

        if (ZonedDateTime.now().isAfter(serverTime)) {
            serverTime = serverTime.plusDays(1);
        }

        try {
            ZonedDateTime finalServerTime = serverTime;
            response = Slack.getInstance().methods(slackBotToken)
                    .chatScheduleMessage(r -> r.postAt((int) finalServerTime.toEpochSecond())
                            .text(message)
                            .channel(channelId));
        } catch (IOException | SlackApiException e) {
            throw new SlackException(e.getMessage());
        }

        if(!(response == null) && response.isOk()) {
            return response;
        } else {
            throw new SlackException("메세지 예약에 실패했습니다.");
        }
    }

    private void deleteMessage(String channelId, String messageId) {
        ChatDeleteScheduledMessageResponse response;

        try {
            response = Slack.getInstance().methods(slackBotToken)
                    .chatDeleteScheduledMessage(r -> r.scheduledMessageId(messageId)
                            .channel(channelId));

            log.info(response.toString());
        } catch (IOException | SlackApiException e) {
            throw new SlackException("예약 메세지 삭제에 실패했습니다.");
        }

        if(!response.isOk() && response.getError().equals("invalid_scheduled_message_id")) {
            throw new SlackException("존재하지 않거나 이미 전송된 메세지 입니다.");
        }

    }

}
