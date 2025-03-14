package com.owl_express.ai.application.dtos;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MessageFindResponseDto {

    private String messageId;
    private String message;
    private String createdAt;

}
