package com.owl_express.ai.common.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CompanyDeliverMessageCreateRequestDto {
    private UUID deliverId;
    private String deliverName;
    private String deliverChannelId;
    private UUID orderId;
    private String productInfo;
    private String ordererName;
    private String startHubName;
    private String orderDescription;
    private String shippingAddress;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime requestArrivalTime;

    @Builder
    public CompanyDeliverMessageCreateRequestDto(
            UUID deliverId,
            String deliverName,
            String deliverChannelId,
            UUID orderId,
            String productInfo,
            String ordererName,
            String startHubName,
            String orderDescription,
            String shippingAddress,
            LocalDateTime requestArrivalTime
    ) {
        this.deliverId = deliverId;
        this.deliverName = deliverName;
        this.deliverChannelId = deliverChannelId;
        this.orderId = orderId;
        this.productInfo = productInfo;
        this.ordererName = ordererName;
        this.startHubName = startHubName;
        this.orderDescription = orderDescription;
        this.shippingAddress = shippingAddress;
        this.requestArrivalTime = requestArrivalTime;
    }
}
