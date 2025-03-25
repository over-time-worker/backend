package com.owl_express.alarm.application.service;


import static com.owl_express.alarm.common.exception.ExceptionMessage.ALARM_NOT_FOUND_MESSAGE;
import static com.owl_express.alarm.common.exception.ExceptionMessage.MESSAGE_DELETE_FAIL_MESSAGE;
import static com.owl_express.alarm.common.exception.ExceptionMessage.MESSAGE_NOT_FOUND_MESSAGE;
import static com.owl_express.alarm.common.exception.ExceptionMessage.MESSAGE_RESERVATION_FAIL_MESSAGE;
import static com.owl_express.alarm.common.exception.ExceptionMessage.MESSAGE_SEND_FAIL_MESSAGE;

import com.owl_express.alarm.common.dto.request.AlarmCreateRequestDto;
import com.owl_express.alarm.common.dto.request.HubDeliverFallbackMessageCreateRequestDto;
import com.owl_express.alarm.common.dto.response.AlarmCreateResponseDto;
import com.owl_express.alarm.common.dto.response.AlarmFindResponseDto;
import com.owl_express.alarm.common.dto.response.AlarmSearchResponseDto;
import com.owl_express.alarm.common.dto.response.MessageCreateResponseDto;
import com.owl_express.alarm.application.exceptions.AlarmException.AlarmNotFoundException;
import com.owl_express.alarm.application.exceptions.AlarmException.SlackException;
import com.owl_express.alarm.common.helper.PassportHelper;
import com.owl_express.alarm.common.util.PageUtil;
import com.owl_express.alarm.domain.entity.Alarm;
import com.owl_express.alarm.domain.entity.constant.MessageType;
import com.owl_express.alarm.domain.entity.constant.PlatformType;
import com.owl_express.alarm.domain.repository.AlarmRepository;
import com.owl_express.alarm.presentation.AlarmService;
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
    private final AlarmUsecaseImpl alarmUsecaseImpl;
    private final PassportHelper passportHelper;

    @Value("${slack.token}")
    private String slackBotToken;

    @Value("${massage.fallback}")
    private String fallbackBase;

    @Override
    @Transactional
    public AlarmCreateResponseDto createAlarmForHubDeliver(
            AlarmCreateRequestDto requestDto,
            String passport
    ) {
        MessageCreateResponseDto messageCreateResponseDto = alarmUsecaseImpl.getHubDeliverMessageFromAi(requestDto, passport);

        //slack 전송
        PlatformType platformType = PlatformType.getType(requestDto.getPlatformName());
        ChatPostMessageResponse chatPostMessageResponse;

        if(platformType.equals(PlatformType.SLACK)) {
            chatPostMessageResponse = sendMessage(messageCreateResponseDto.getMessage(),
                    requestDto.getDeliverChannelId());

            String gmtDate = chatPostMessageResponse.getHttpResponseHeaders().get("date").get(0);

            //alarm 생성
            Alarm alarm = Alarm.create(
                    messageCreateResponseDto,
                    requestDto,
                    platformType,
                    gmtDate,
                    null,
                    MessageType.NORMAL,
                    passportHelper.getPassportDto(passport).getUserId()
            );

            alarmRepository.save(alarm);

            return AlarmCreateResponseDto.toDto(alarm, null);
        }
        return AlarmCreateResponseDto.builder().build();
    }

    @Override
    @Transactional
    public AlarmCreateResponseDto createAlarmForCompanyDeliver(
            AlarmCreateRequestDto requestDto,
            String passport
    ) {
        MessageCreateResponseDto messageCreateResponseDto = alarmUsecaseImpl.getCompanyDeliverMessageFromAi(requestDto, passport);

        //slack 전송
        PlatformType platformType = PlatformType.getType(requestDto.getPlatformName());

        if(platformType.equals(PlatformType.SLACK)) {
            ChatScheduleMessageResponse chatScheduleMessageResponse
                    = scheduleMessage(messageCreateResponseDto.getMessage(), requestDto.getDeliverChannelId());

            String platformMessageId = chatScheduleMessageResponse.getScheduledMessageId();
            String gmtDate = chatScheduleMessageResponse.getHttpResponseHeaders().get("date").get(0);

            //alarm 생성
            Alarm alarm = Alarm.create(
                    messageCreateResponseDto,
                    requestDto,
                    platformType,
                    gmtDate,
                    platformMessageId,
                    MessageType.RESERVATION,
                    passportHelper.getPassportDto(passport).getUserId()
            );

            alarmRepository.save(alarm);

            return AlarmCreateResponseDto.toDto(alarm, platformMessageId);
        }
        return AlarmCreateResponseDto.builder().build();
    }

    @Override
    @Transactional
    public void delete(
            String channelId,
            String messageId,
            String passport
    ) {
        deleteMessage(channelId, messageId);

        Alarm alarm = alarmRepository.findByMessageId(messageId).orElseThrow(
                () -> new AlarmNotFoundException(ALARM_NOT_FOUND_MESSAGE));

        alarmUsecaseImpl.deleteMessageToAi(alarm.getAiId(), passport);

        alarm.deleteEntity(passportHelper.getPassportDto(passport).getUserId());
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

    @Override
    @Transactional
    public void hubFallback(
            HubDeliverFallbackMessageCreateRequestDto requestDto,
            String passport
    ) {
        PlatformType platformType = PlatformType.getType(requestDto.getPlatformName());

        Alarm alarm = alarmRepository.findByAiId(requestDto.getAiId()).orElseThrow(
                () -> new AlarmNotFoundException(ALARM_NOT_FOUND_MESSAGE));

        alarmUsecaseImpl.deleteMessageToAi(alarm.getAiId(), passport);

        alarm.deleteEntity(passportHelper.getPassportDto(passport).getUserId());

        ChatPostMessageResponse chatPostMessageResponse;
        String fallbackMessage = createFallbackMessage(requestDto);

        if(platformType.equals(PlatformType.SLACK)) {
            chatPostMessageResponse = sendMessage(
                    fallbackMessage,
                    requestDto.getDeliverChannelId()
            );

            String gmtDate = chatPostMessageResponse.getHttpResponseHeaders().get("date").get(0);

            //alarm 생성
            Alarm newAlarm = Alarm.createFallback(
                    requestDto,
                    platformType,
                    gmtDate,
                    null,
                    MessageType.NORMAL,
                    fallbackMessage,
                    passportHelper.getPassportDto(passport).getUserId()
            );

            alarmRepository.save(newAlarm);
        }
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

    private String createFallbackMessage(HubDeliverFallbackMessageCreateRequestDto requestDto) {
        return fallbackBase
                .replace("{deliverName}", requestDto.getDeliverName())
                .replace("{orderId}", requestDto.getOrderId().toString())
                .replace("{productInfo}", requestDto.getProductInfo());
    }

}
