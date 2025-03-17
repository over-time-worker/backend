package com.owl_express.alarm.application.dtos.response;

import com.owl_express.alarm.domain.entity.Notification;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AlarmCreateResponseDto {
    private String platformMessageId;
    private UUID alarmId;
    private String message;

    @Builder
    public AlarmCreateResponseDto(
            String platformMessageId,
            UUID alarmId,
            String message
    ) {
        this.platformMessageId = platformMessageId;
        this.alarmId = alarmId;
        this.message = message;
    }

    public static AlarmCreateResponseDto toDto(
            Notification notification,
            String platformMessageId
    ) {
        return AlarmCreateResponseDto.builder()
                .platformMessageId(platformMessageId)
                .alarmId(notification.getId())
                .message(notification.getMessage())
                .build();
    }
}
