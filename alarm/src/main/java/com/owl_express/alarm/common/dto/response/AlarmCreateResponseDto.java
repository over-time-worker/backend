package com.owl_express.alarm.common.dto.response;

import com.owl_express.alarm.domain.entity.Alarm;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AlarmCreateResponseDto {
    private UUID aiId;
    private String platformMessageId;
    private UUID alarmId;
    private String message;

    @Builder
    public AlarmCreateResponseDto(
            UUID aiId,
            String platformMessageId,
            UUID alarmId,
            String message
    ) {
        this.aiId = aiId;
        this.platformMessageId = platformMessageId;
        this.alarmId = alarmId;
        this.message = message;
    }

    public static AlarmCreateResponseDto toDto(
            Alarm alarm,
            String platformMessageId
    ) {
        return AlarmCreateResponseDto.builder()
                .aiId(alarm.getAiId())
                .platformMessageId(platformMessageId)
                .alarmId(alarm.getId())
                .message(alarm.getMessage())
                .build();
    }
}
