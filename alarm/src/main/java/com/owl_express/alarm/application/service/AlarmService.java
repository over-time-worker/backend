package com.owl_express.alarm.application.service;

import com.owl_express.alarm.application.dtos.request.AlarmCreateRequestDto;
import org.springframework.stereotype.Service;

@Service
public interface AlarmService {
    void createAlarmForHubDeliver(AlarmCreateRequestDto alarmCreateRequestDto);
}
