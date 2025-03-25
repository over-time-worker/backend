package com.owl_express.alarm.common.dto.response;

import com.owl_express.alarm.domain.entity.Alarm;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AlarmFindResponseDto {
    private UUID alarmId;
    private Long userId;
    private String platformType;
    private String userChannelId;
    private String message;
    private String messageType;

    @Builder
    public AlarmFindResponseDto(
            UUID alarmId,
            Long userId,
            String platformType,
            String userChannelId,
            String message,
            String messageType
    ) {
        this.alarmId = alarmId;
        this.userId = userId;
        this.platformType = platformType;
        this.userChannelId = userChannelId;
        this.message = message;
        this.messageType = messageType;
    }

    public static AlarmFindResponseDto toDto(Alarm alarm) {
        return AlarmFindResponseDto.builder()
                .alarmId(alarm.getId())
                .userId(alarm.getUserId())
                .platformType(alarm.getPlatformType().name())
                .userChannelId(alarm.getUserChannelId())
                .message(alarm.getMessage())
                .messageType(alarm.getMessageType().name())
                .build();

    }
}
