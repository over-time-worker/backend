package com.owl_express.alarm.presentation;

import com.owl_express.alarm.application.dtos.CommonDto;
import com.owl_express.alarm.application.dtos.request.AlarmCreateRequestDto;
import com.owl_express.alarm.application.dtos.response.AlarmCreateResponseDto;
import com.owl_express.alarm.application.service.AlarmService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
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

    @PostMapping("/company-delivery")
    public ResponseEntity<CommonDto<AlarmCreateResponseDto>> createAlarmForCompanyDeliver(
            @Valid @RequestBody AlarmCreateRequestDto alarmCreateRequestDto
    ) {
        AlarmCreateResponseDto response = alarmService.createAlarmForCompanyDeliver(alarmCreateRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                CommonDto.<AlarmCreateResponseDto>builder()
                        .status(HttpStatus.CREATED)
                        .message("알림 예약 완료")
                        .code(HttpStatus.CREATED.value())
                        .data(response)
                        .build());
    }

    @DeleteMapping("/channel/{channel_id}/message/{message_id}")
    public ResponseEntity<CommonDto<Void>> deleteAlarm(
            @NotNull(message = "[notNull:channel_id]") @PathVariable("channel_id") String channelId,
            @NotNull(message = "[notNull:message_id]") @PathVariable("message_id") String messageId
    ) {
        alarmService.deleteAlarm(channelId, messageId);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(
                CommonDto.<Void>builder()
                        .status(HttpStatus.ACCEPTED)
                        .message("예약 메세지 삭제 완료")
                        .code(HttpStatus.ACCEPTED.value())
                        .data(null)
                        .build());
    }



}
