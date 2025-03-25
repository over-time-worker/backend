package com.owl_express.alarm.common.dto.response;

import com.owl_express.alarm.domain.entity.Alarm;
import com.owl_express.alarm.domain.entity.constant.MessageType;
import com.owl_express.alarm.domain.entity.constant.PlatformType;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AlarmSearchResponseDto {
    private UUID alarmId;
    private Long userId;
    private PlatformType platformType;
    private String userChannelId;
    private String message;
    private MessageType messageType;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Builder
    public AlarmSearchResponseDto(
            UUID alarmId,
            Long userId,
            PlatformType platformType,
            String userChannelId,
            String message,
            MessageType messageType,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        this.alarmId = alarmId;
        this.userId = userId;
        this.platformType = platformType;
        this.userChannelId = userChannelId;
        this.message = message;
        this.messageType = messageType;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static AlarmSearchResponseDto toDto(Alarm alarm) {
        return AlarmSearchResponseDto.builder()
                .alarmId(alarm.getId())
                .userId(alarm.getUserId())
                .platformType(alarm.getPlatformType())
                .userChannelId(alarm.getUserChannelId())
                .message(alarm.getMessage())
                .messageType(alarm.getMessageType())
                .createdAt(alarm.getCreatedAt())
                .updatedAt(alarm.getModifiedAt())
                .build();

    }
}