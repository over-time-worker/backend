package com.owl_express.alarm.application.service;

import com.owl_express.alarm.application.dtos.request.AlarmCreateRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AlarmServiceImpl implements AlarmService {

    @Override
    public void createAlarmForHubDeliver(AlarmCreateRequestDto requestDto) {

    }

}
