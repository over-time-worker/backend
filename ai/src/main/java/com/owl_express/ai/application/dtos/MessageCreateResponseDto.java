package com.owl_express.ai.application.dtos;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
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
