package com.owl_express.alarm.application.dtos.request;

import com.owl_express.alarm.application.dtos.request.AlarmCreateRequestDto.HubListDto;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HubDeliverMessageCreateRequestDto {

    private UUID deliverId;
    private String deliverName;
    private String deliverChannelId;
    private UUID orderId;
    private String productInfo;
    private String ordererName;
    private String startHubName;
    private String endHubName;
    private String currentHubName;
    private String nextHubName;
    private String orderDescription;
    private String shippingAddress;
    private LocalDateTime requestArrivalTime;
    private Double totalEstimateDistance;
    private Duration totalEstimateDurationTime;
    private List<HubListDto> totalHubList;

    @Builder
    public HubDeliverMessageCreateRequestDto(
            UUID deliverId,
            String deliverName,
            String deliverChannelId,
            UUID orderId,
            String productInfo,
            String ordererName,
            String startHubName,
            String endHubName,
            String currentHubName,
            String nextHubName,
            String orderDescription,
            String shippingAddress,
            LocalDateTime requestArrivalTime,
            Double totalEstimateDistance,
            Duration totalEstimateDurationTime,
            List<HubListDto> totalHubList) {
        this.deliverId = deliverId;
        this.deliverName = deliverName;
        this.deliverChannelId = deliverChannelId;
        this.orderId = orderId;
        this.productInfo = productInfo;
        this.ordererName = ordererName;
        this.startHubName = startHubName;
        this.endHubName = endHubName;
        this.currentHubName = currentHubName;
        this.nextHubName = nextHubName;
        this.orderDescription = orderDescription;
        this.shippingAddress = shippingAddress;
        this.requestArrivalTime = requestArrivalTime;
        this.totalEstimateDistance = totalEstimateDistance;
        this.totalEstimateDurationTime = totalEstimateDurationTime;
        this.totalHubList = totalHubList;
    }

    public static HubDeliverMessageCreateRequestDto AlarmDtoToMessageDto(AlarmCreateRequestDto requestDto) {
        return HubDeliverMessageCreateRequestDto.builder()
                .deliverId(requestDto.getDeliverId())
                .deliverName(requestDto.getDeliverName())
                .deliverChannelId(requestDto.getDeliverChannelId())
                .orderId(requestDto.getOrderId())
                .productInfo(requestDto.getProductInfo())
                .ordererName(requestDto.getOrdererName())
                .startHubName(requestDto.getStartHubName())
                .endHubName(requestDto.getEndHubName())
                .orderDescription(requestDto.getOrderDescription())
                .shippingAddress(requestDto.getShippingAddress())
                .requestArrivalTime(requestDto.getRequestArrivalTime())
                .totalEstimateDistance(requestDto.getTotalEstimateDistance())
                .totalEstimateDurationTime(requestDto.getTotalEstimateDurationTime())
                .totalHubList(requestDto.getTotalHubList())
                .build();
    }
}