package com.owl_express.ai.application.dtos.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class MessageCreateRequestDto {

    @Size(min = 1, max = 50, message = "[size:orderId]")
    private String orderId;
    @Size(min = 1, max = 50, message = "[size:ordererName]")
    private String ordererName;
    @NotBlank(message = "[notBlank:productInfo]")
    private String productInfo;
    @Size(min = 1, max = 50, message = "[size:startHub]")
    private String startHub;
    @Size(min = 1, max = 255, message = "[size:destination]")
    private String destination;
    @NotNull(message = "[notBlank:deliverId]")
    private UUID deliverId;
    @Size(min = 1, max = 50, message = "[size:deliverName]")
    private String deliverName;
    @Size(min = 1, max = 50, message = "[size:deliverChannelId]")
    private String deliverChannelId;
    private String orderDescription;
    @NotBlank(message = "[notBlank:departureDeadline]")
    private String departureDeadline;

}