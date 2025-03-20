package com.owl_express.alarm.application.dtos.request;

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

    public static CompanyDeliverMessageCreateRequestDto alarmDtoToMessageDto(AlarmCreateRequestDto requestDto) {
        return CompanyDeliverMessageCreateRequestDto.builder()
                .deliverId(requestDto.getDeliverId())
                .deliverName(requestDto.getDeliverName())
                .deliverChannelId(requestDto.getDeliverChannelId())
                .orderId(requestDto.getOrderId())
                .productInfo(requestDto.getProductInfo())
                .ordererName(requestDto.getOrdererName())
                .startHubName(requestDto.getStartHubName())
                .orderDescription(requestDto.getOrderDescription())
                .shippingAddress(requestDto.getShippingAddress())
                .requestArrivalTime(requestDto.getRequestArrivalTime())
                .build();
    }
}
