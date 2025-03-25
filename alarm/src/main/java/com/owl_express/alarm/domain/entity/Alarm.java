package com.owl_express.alarm.domain.entity;

import com.owl_express.alarm.common.dto.request.AlarmCreateRequestDto;
import com.owl_express.alarm.common.dto.request.HubDeliverFallbackMessageCreateRequestDto;
import com.owl_express.alarm.common.dto.response.MessageCreateResponseDto;
import com.owl_express.alarm.application.exceptions.AlarmException.NotSupportedPlatformTypeException;
import com.owl_express.alarm.common.util.CommonUtil;
import com.owl_express.alarm.domain.entity.constant.MessageType;
import com.owl_express.alarm.domain.entity.constant.PlatformType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

@Getter
@Entity
@Table(name = "p_alarm")
@SQLRestriction("deleted_at is null")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Alarm extends BaseEntity {
    @Id
    @Column(name = "alarm_id", length = 50)
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "user_id", length = 50, nullable = false)
    private Long userId;

    @Column(name = "user_channel_id", length = 50, nullable = false)
    private String userChannelId;

    @Enumerated(EnumType.STRING)
    @Column(name = "platform_type", length = 50, nullable = false)
    private PlatformType platformType;

    @Column(name = "message", columnDefinition = "TEXT")
    private String message;

    @Column(name = "send_at")
    private LocalDateTime sendAt;

    @Column(name = "ai_id", length = 50, nullable = false)
    private UUID aiId;

    @Column(name = "message_id")
    private String messageId;

    @Enumerated(EnumType.STRING)
    @Column(name = "message_type", nullable = false)
    private MessageType messageType;

    @Builder
    public Alarm(
            Long userId,
            String userChannelId,
            PlatformType platformType,
            String message,
            LocalDateTime sendAt,
            UUID aiId,
            String messageId,
            MessageType messageType
    ) {
        this.userId = userId;
        this.userChannelId = userChannelId;
        this.platformType = platformType;
        this.message = message;
        this.sendAt = sendAt;
        this.aiId = aiId;
        this.messageId = messageId;
        this.messageType = messageType;
    }

    public static Alarm create(
            MessageCreateResponseDto messageCreateResponseDto,
            AlarmCreateRequestDto requestDto,
            PlatformType platformType,
            String gmtDate,
            String platformMessageId,
            MessageType messageType,
            Long userId
    ) {
        Alarm alarm = Alarm.builder()
                .aiId(messageCreateResponseDto.getAiId())
                .userId(requestDto.getDeliverUserId())
                .userChannelId(requestDto.getDeliverChannelId())
                .platformType(platformType)
                .message(messageCreateResponseDto.getMessage())
                .sendAt(CommonUtil.gmtStringToDefaultLocalDateTime(gmtDate))
                .messageType(messageType)
                .messageId(platformMessageId)
                .build();

        alarm.createdEntity(userId);

        return alarm;
    }

    public static Alarm createFallback(
            HubDeliverFallbackMessageCreateRequestDto requestDto,
            PlatformType platformType,
            String gmtDate,
            String platformMessageId,
            MessageType messageType,
            String message,
            Long userId
    ) {
        Alarm alarm = Alarm.builder()
                .aiId(requestDto.getAiId())
                .userId(requestDto.getDeliverUserId())
                .userChannelId(requestDto.getDeliverChannelId())
                .platformType(platformType)
                .message(message)
                .sendAt(CommonUtil.gmtStringToDefaultLocalDateTime(gmtDate))
                .messageType(messageType)
                .messageId(platformMessageId)
                .build();

        alarm.createdEntity(userId);

        return alarm;
    }

}

