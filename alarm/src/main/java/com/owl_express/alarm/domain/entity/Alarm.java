package com.owl_express.alarm.domain.entity;

import com.owl_express.alarm.application.exceptions.AlarmException.NotSupportedPlatformTypeException;
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

    @RequiredArgsConstructor
    public enum PlatformType{
        SLACK("slack");

        private final String value;

        public static PlatformType getType(String type) {
            for(PlatformType pt : PlatformType.values()) {
                if(pt.value.equalsIgnoreCase(type)) {
                    return pt;
                }
            }
            throw new NotSupportedPlatformTypeException("지원하지 않는 플랫폼 타입 입니다." + type);
        }
    }

    @RequiredArgsConstructor
    public enum MessageType{
        NORMAL("일반 메세지"),
        RESERVATION("예약 메세지");

        private final String value;

        public static MessageType getType(String type) {
            for(MessageType mt : MessageType.values()) {
                if(mt.value.equalsIgnoreCase(type)) {
                    return mt;
                }
            }
            throw new NotSupportedPlatformTypeException("지원하지 않는 메세지 타입 입니다." + type);
        }
    }
}

