package com.owl_express.alarm.application.dtos.response;

import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MessageCreateResponseDto {
    private UUID aiId;
    private String message;

    @Builder
    public MessageCreateResponseDto(
            UUID aiId,
            String message
    ) {
        this.aiId = aiId;
        this.message = message;
    }
}
