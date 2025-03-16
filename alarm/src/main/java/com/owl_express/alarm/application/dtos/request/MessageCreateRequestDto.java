package com.owl_express.alarm.application.dtos.request;

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

    private String orderId;
    private String ordererName;
    private String productInfo;
    private String startHub;
    private String destination;
    private UUID deliverId;
    private String deliverName;
    private String deliverPlatformId;
    private String orderDescription;
    private String departureDeadline;

}