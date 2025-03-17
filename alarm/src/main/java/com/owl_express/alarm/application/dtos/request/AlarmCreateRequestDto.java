package com.owl_express.alarm.application.dtos.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AlarmCreateRequestDto {
    @NotNull(message = "[notNull:deliverId]")
    private UUID deliverId;
    @NotNull(message = "[notNull:userId]")
    private Long userId;
    @Size(min = 1, max = 50, message = "[size:deliverName]")
    private String deliverName;
    @Size(min = 1, max = 50, message = "[size:deliverPlatformId]")
    private String deliverChannelId;
    @Size(min = 1, max = 50, message = "[size:platformName]")
    private String platformName;
    @NotNull(message = "[notNull:orderId]")
    private UUID orderId;
    @Size(min = 1, max = 50, message = "[size:ordererName]")
    private String ordererName;
    @Size(min = 1, max = 50, message = "[size:startHub]")
    private String startHub;
    @Size(min = 1, max = 50, message = "[size:destination]")
    private String destination;
    private String orderDescription;
    @NotBlank(message = "[notBlank:departureDeadline]")
    private String departureDeadline;

}
