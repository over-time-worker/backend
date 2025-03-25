package com.owl_express.ai.common.dto.response;

import com.owl_express.ai.domain.entity.Ai;
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

    public static MessageCreateResponseDto toDto(Ai ai) {
        return MessageCreateResponseDto.builder()
                .aiId(ai.getId())
                .message(ai.getResponse())
                .build();
    }
}
