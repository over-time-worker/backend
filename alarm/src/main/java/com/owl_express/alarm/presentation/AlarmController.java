package com.owl_express.alarm.presentation;

import static com.owl_express.alarm.presentation.ApiResponseMessageConstant.DELETE_MESSAGE_SUCCESS;
import static com.owl_express.alarm.presentation.ApiResponseMessageConstant.GET_ALARM_SUCCESS;
import static com.owl_express.alarm.presentation.ApiResponseMessageConstant.RESERVE_MESSAGE_SUCCESS;
import static com.owl_express.alarm.presentation.ApiResponseMessageConstant.SEND_MESSAGE_SUCCESS;

import com.owl_express.alarm.application.dtos.CommonDto;
import com.owl_express.alarm.application.dtos.request.AlarmCreateRequestDto;
import com.owl_express.alarm.application.dtos.response.AlarmCreateResponseDto;
import com.owl_express.alarm.application.dtos.response.AlarmFindResponseDto;
import com.owl_express.alarm.application.service.AlarmService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/api/alarms")
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
                        .message(SEND_MESSAGE_SUCCESS)
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
                        .message(RESERVE_MESSAGE_SUCCESS)
                        .code(HttpStatus.CREATED.value())
                        .data(response)
                        .build());
    }

    @DeleteMapping("/channel/{channel_id}/message/{message_id}")
    public ResponseEntity<CommonDto<Void>> delete(
            @NotNull(message = "[notNull:channel_id]") @PathVariable("channel_id") String channelId,
            @NotNull(message = "[notNull:message_id]") @PathVariable("message_id") String messageId
    ) {
        alarmService.delete(channelId, messageId);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(
                CommonDto.<Void>builder()
                        .status(HttpStatus.ACCEPTED)
                        .message(DELETE_MESSAGE_SUCCESS)
                        .code(HttpStatus.ACCEPTED.value())
                        .data(null)
                        .build());
    }

    @GetMapping("/{alarm_id}")
    public ResponseEntity<CommonDto<AlarmFindResponseDto>> find(
            @NotNull(message = "[notNull:alarm_id]") @PathVariable("alarm_id") UUID alarmId
    ) {
        AlarmFindResponseDto response = alarmService.find(alarmId);

        return ResponseEntity.status(HttpStatus.OK).body(
                CommonDto.<AlarmFindResponseDto>builder()
                        .status(HttpStatus.OK)
                        .message(GET_ALARM_SUCCESS)
                        .code(HttpStatus.OK.value())
                        .data(response)
                        .build());
    }



}
