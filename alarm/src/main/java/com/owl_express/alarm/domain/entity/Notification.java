package com.owl_express.alarm.domain.entity;

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

@Getter
@Entity
@Table(name = "p_notification")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification extends BaseEntity {
    @Id
    @Column(name = "notification_id", length = 50)
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "user_id", length = 50, nullable = false)
    private Long userId;

    @Column(name = "user_platform_id", length = 50, nullable = false)
    private String userPlatformId;

    @Enumerated(EnumType.STRING)
    @Column(name = "platform_type", length = 50, nullable = false)
    private PlatformType platformType;

    @Column(name = "message", columnDefinition = "TEXT")
    private String message;

    @Column(name = "send_at")
    private LocalDateTime sendAt;

    @Column(name = "is_send", nullable = false)
    private Boolean isSend = false;

    @Column(name = "ai_id", length = 50, nullable = false)
    private UUID aiId;

    @Builder
    public Notification(
            Long userId,
            String userPlatformId,
            PlatformType platformType,
            String message,
            LocalDateTime sendAt,
            Boolean isSend,
            UUID aiId
    ) {
        this.userId = userId;
        this.userPlatformId = userPlatformId;
        this.platformType = platformType;
        this.message = message;
        this.sendAt = sendAt;
        this.isSend = isSend;
        this.aiId = aiId;
    }

    @RequiredArgsConstructor
    public enum PlatformType{
        SLACK("slack");

        private final String value;
    }
}

