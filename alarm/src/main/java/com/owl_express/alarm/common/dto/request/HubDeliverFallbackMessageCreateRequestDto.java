package com.owl_express.alarm.common.dto.request;

import jakarta.validation.constraints.Size;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HubDeliverFallbackMessageCreateRequestDto {
    private UUID aiId;
    private String messageId;
    private UUID deliverId;
    private Long deliverUserId;
    private String deliverName;
    private String deliverChannelId;
    private String platformName;
    private UUID deliveryId;
    private UUID orderId;
    private String productInfo;
    @Size(min = 1, max = 50, message = "[size:ordererName]")
    private String ordererName;
    @Size(min = 1, max = 50, message = "[size:startHubName]")
    private String shippingAddress;

    @Builder
    public HubDeliverFallbackMessageCreateRequestDto(
            UUID aiId,
            String messageId,
            UUID deliverId,
            Long deliverUserId,
            String deliverName,
            String deliverChannelId,
            String platformName,
            UUID deliveryId,
            UUID orderId,
            String productInfo,
            String ordererName,
            String shippingAddress
    ) {
        this.aiId = aiId;
        this.messageId = messageId;
        this.deliverId = deliverId;
        this.deliverUserId = deliverUserId;
        this.deliverName = deliverName;
        this.deliverChannelId = deliverChannelId;
        this.platformName = platformName;
        this.deliveryId = deliveryId;
        this.orderId = orderId;
        this.productInfo = productInfo;
        this.ordererName = ordererName;
        this.shippingAddress = shippingAddress;
    }
}
