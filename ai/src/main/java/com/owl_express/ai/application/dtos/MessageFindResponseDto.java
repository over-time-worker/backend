package com.owl_express.ai.application.dtos;

import com.owl_express.ai.common.util.CommonUtil;
import com.owl_express.ai.domain.entity.Ai;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MessageFindResponseDto {

    private UUID messageId;
    private String message;
    private String createdAt;

    @Builder
    public MessageFindResponseDto(UUID messageId, String message, String createdAt) {
        this.messageId = messageId;
        this.message = message;
        this.createdAt = createdAt;
    }

    public static MessageFindResponseDto toDto(Ai ai) {
        return MessageFindResponseDto.builder()
                .messageId(ai.getId())
                .message(ai.getResponse())
                .createdAt(CommonUtil.LocalDateTimetoString(ai.getCreatedAt()))
                .build();
    }

}
