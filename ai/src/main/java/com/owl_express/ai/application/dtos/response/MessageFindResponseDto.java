package com.owl_express.ai.application.dtos.response;

import com.owl_express.ai.common.util.CommonUtil;
import com.owl_express.ai.domain.entity.Ai;
import java.time.LocalDateTime;
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
    private String modifiedAt;

    @Builder
    public MessageFindResponseDto(UUID messageId, String message, String createdAt, String modifiedAt) {
        this.messageId = messageId;
        this.message = message;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }

    @Builder
    public MessageFindResponseDto(UUID messageId, String message, LocalDateTime createdAt, LocalDateTime modifiedAt) {
        this.messageId = messageId;
        this.message = message;
        this.createdAt = CommonUtil.LocalDateTimetoString(createdAt);
        this.modifiedAt = CommonUtil.LocalDateTimetoString(modifiedAt);
    }

    public static MessageFindResponseDto toDto(Ai ai) {
        return MessageFindResponseDto.builder()
                .messageId(ai.getId())
                .message(ai.getResponse())
                .createdAt(CommonUtil.LocalDateTimetoString(ai.getCreatedAt()))
                .modifiedAt(CommonUtil.LocalDateTimetoString(ai.getModifiedAt()))
                .build();
    }

}
