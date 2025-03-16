package com.owl_express.alarm.presentation;

import com.owl_express.alarm.application.dtos.CommonDto;
import com.owl_express.alarm.application.dtos.request.AlarmCreateRequestDto;
import com.owl_express.alarm.application.service.AlarmService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class AlarmController {

    private final AlarmService alarmService;

    @PostMapping("/hub-delivery")
    public ResponseEntity<CommonDto<Void>> createAlarmForHubDeliver(
            @Valid @RequestBody AlarmCreateRequestDto alarmCreateRequestDto
    ) {
        alarmService.createAlarmForHubDeliver(alarmCreateRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                CommonDto.<Void>builder()
                        .status(HttpStatus.CREATED)
                        .message("알림 전송 완료")
                        .code(HttpStatus.CREATED.value())
                        .data(null)
                        .build());
    }
}
