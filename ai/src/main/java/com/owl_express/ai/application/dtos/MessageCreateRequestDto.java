package com.owl_express.ai.application.dtos;

import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class MessageCreateRequestDto {

    private UUID orderId;
    private String ordererName;
    private String productInfo;
    private String startHub;
    private String endHub;
    private UUID deliverId;
    private String deliverName;
    private String deliverPlatformId;
    private String orderDescription;
    private String departureDeadline;

}