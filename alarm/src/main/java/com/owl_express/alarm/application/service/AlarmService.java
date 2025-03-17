package com.owl_express.alarm.application.service;

import com.owl_express.alarm.application.dtos.request.AlarmCreateRequestDto;
import com.owl_express.alarm.application.dtos.response.AlarmCreateResponseDto;
import org.springframework.stereotype.Service;

@Service
public interface AlarmService {
    void createAlarmForHubDeliver(AlarmCreateRequestDto alarmCreateRequestDto);

    AlarmCreateResponseDto createAlarmForCompanyDeliver(AlarmCreateRequestDto alarmCreateRequestDto);

    void deleteAlarm(String channelId, String messageId);
}
