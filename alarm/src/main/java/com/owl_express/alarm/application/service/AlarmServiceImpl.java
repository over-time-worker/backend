package com.owl_express.alarm.application.service;


import static com.owl_express.alarm.common.exception.ExceptionMessage.ALARM_NOT_FOUND_MESSAGE;
import static com.owl_express.alarm.common.exception.ExceptionMessage.MESSAGE_DELETE_FAIL_MESSAGE;
import static com.owl_express.alarm.common.exception.ExceptionMessage.MESSAGE_NOT_FOUND_MESSAGE;
import static com.owl_express.alarm.common.exception.ExceptionMessage.MESSAGE_RESERVATION_FAIL_MESSAGE;
import static com.owl_express.alarm.common.exception.ExceptionMessage.MESSAGE_SEND_FAIL_MESSAGE;

import com.owl_express.alarm.application.dtos.request.AlarmCreateRequestDto;
import com.owl_express.alarm.application.dtos.response.AlarmCreateResponseDto;
import com.owl_express.alarm.application.dtos.response.AlarmFindResponseDto;
import com.owl_express.alarm.application.dtos.response.AlarmSearchResponseDto;
import com.owl_express.alarm.application.dtos.response.MessageCreateResponseDto;
import com.owl_express.alarm.application.exceptions.AlarmException.AlarmNotFoundException;
import com.owl_express.alarm.application.exceptions.AlarmException.SlackException;
import com.owl_express.alarm.common.util.CommonUtil;
import com.owl_express.alarm.common.util.PageUtil;
import com.owl_express.alarm.domain.entity.Alarm;
import com.owl_express.alarm.domain.entity.Alarm.MessageType;
import com.owl_express.alarm.domain.entity.Alarm.PlatformType;
import com.owl_express.alarm.domain.repository.AlarmRepository;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AlarmServiceImpl implements AlarmService {

    public static final String INVALID_SLACK_MESSAGE = "invalid_scheduled_message_id";
    public static final String KOREA_ZONE_ID = "Asia/Seoul";

    private final AlarmRepository alarmRepository;
    private final AlarmUsecase alarmUsecase;

    @Value("${slack.token}")
    private String slackBotToken;

    @Override
    @Transactional
    public void createAlarmForHubDeliver(AlarmCreateRequestDto requestDto) {
        // TODO : ai 메세지 요청 feign client 통신 test
        MessageCreateResponseDto messageCreateResponseDto = alarmUsecase.getHubDeliverMessageFromAi(requestDto);

        //slack 전송
        PlatformType platformType = PlatformType.getType(requestDto.getPlatformName());
        ChatPostMessageResponse chatPostMessageResponse;

        if(platformType.equals(PlatformType.SLACK)) {
            chatPostMessageResponse = sendMessage(messageCreateResponseDto.getMessage(),
                    requestDto.getDeliverChannelId());

            String gmtDate = chatPostMessageResponse.getHttpResponseHeaders().get("date").get(0);

            //alarm 생성
            Alarm alarm = Alarm.builder()
                    .aiId(messageCreateResponseDto.getAiId())
                    .userId(requestDto.getDeliverUserId())
                    .userChannelId(requestDto.getDeliverChannelId())
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
    @Transactional
    public AlarmCreateResponseDto createAlarmForCompanyDeliver(AlarmCreateRequestDto requestDto) {
        // TODO : ai 메세지 요청 feign client 통신 test
        MessageCreateResponseDto messageCreateResponseDto = alarmUsecase.getCompanyDeliverMessageFromAi(requestDto);

        //slack 전송
        PlatformType platformType = PlatformType.getType(requestDto.getPlatformName());

        if(platformType.equals(PlatformType.SLACK)) {
            ChatScheduleMessageResponse chatScheduleMessageResponse
                    = scheduleMessage(messageCreateResponseDto.getMessage(), requestDto.getDeliverChannelId());

            String platformMessageId = chatScheduleMessageResponse.getScheduledMessageId();
            String gmtDate = chatScheduleMessageResponse.getHttpResponseHeaders().get("date").get(0);

            //alarm 생성
            Alarm alarm = Alarm.builder()
                    .aiId(messageCreateResponseDto.getAiId())
                    .userId(requestDto.getDeliverUserId())
                    .userChannelId(requestDto.getDeliverChannelId())
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
    public void delete(String channelId, String messageId) {
        deleteMessage(channelId, messageId);

        Alarm alarm = alarmRepository.findByMessageId(messageId).orElseThrow(
                () -> new AlarmNotFoundException(ALARM_NOT_FOUND_MESSAGE));

        // TODO : UserId 넣어주기
        alarm.deleteEntity(1L);
        alarmRepository.save(alarm);
    }

    @Override
    public AlarmFindResponseDto find(UUID alarmId) {
        Alarm alarm = alarmRepository.findById(alarmId).orElseThrow(
                () -> new AlarmNotFoundException(ALARM_NOT_FOUND_MESSAGE));

        return AlarmFindResponseDto.toDto(alarm);
    }

    @Override
    public PagedModel<AlarmSearchResponseDto> search(
            int page,
            int size,
            String sort,
            String orderBy,
            String startDate,
            String endDate,
            String deliveryUserId,
            String platformType
    ) {
        Pageable pageable = PageUtil.getPageable(page, size, sort, orderBy);
        Page<AlarmSearchResponseDto> paged = alarmRepository.search(pageable, startDate, endDate, deliveryUserId, platformType);
        return new PagedModel<>(paged);

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
            throw new SlackException(MESSAGE_SEND_FAIL_MESSAGE);
        }
    }

    private ChatScheduleMessageResponse scheduleMessage(String message, String channelId) {
        ChatScheduleMessageResponse response;
        ZonedDateTime koreaTime = ZonedDateTime.of(LocalDate.now(), LocalTime.of(6,0), ZoneId.of(KOREA_ZONE_ID));
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
            throw new SlackException(MESSAGE_RESERVATION_FAIL_MESSAGE);
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
            throw new SlackException(MESSAGE_DELETE_FAIL_MESSAGE);
        }

        if(!response.isOk() && response.getError().equals(INVALID_SLACK_MESSAGE)) {
            throw new SlackException(MESSAGE_NOT_FOUND_MESSAGE);
        }

    }

}
