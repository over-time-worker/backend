package com.owl_express.alarm.application.dtos.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MessageCreateResponseDto {
    private String message;

    @Builder
    public MessageCreateResponseDto(String message) {
        this.message = message;
    }
}
