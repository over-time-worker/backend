package com.owl_express.alarm.application.dtos.request;

import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MessageCreateRequestDto {

    private String orderId;
    private String ordererName;
    private String productInfo;
    private String startHub;
    private String destination;
    private UUID deliverId;
    private String deliverName;
    private String deliverChannelId;
    private String orderDescription;
    private String departureDeadline;

    @Builder
    public MessageCreateRequestDto(
            String orderId,
            String ordererName,
            String productInfo,
            String startHub,
            String destination,
            UUID deliverId,
            String deliverName,
            String deliverChannelId,
            String orderDescription,
            String departureDeadline) {
        this.orderId = orderId;
        this.ordererName = ordererName;
        this.productInfo = productInfo;
        this.startHub = startHub;
        this.destination = destination;
        this.deliverId = deliverId;
        this.deliverName = deliverName;
        this.deliverChannelId = deliverChannelId;
        this.orderDescription = orderDescription;
        this.departureDeadline = departureDeadline;
    }

    // TODO : Order Service 생성 후 OrderFindResponseDto 에서 정보 가져오기
    public static MessageCreateRequestDto AlarmDtoToMessageDto(AlarmCreateRequestDto requestDto, String productInfo) {
        return MessageCreateRequestDto.builder()
                .orderId(requestDto.getOrderId().toString())
                .ordererName(requestDto.getOrdererName())
                .productInfo(productInfo)
                .startHub(requestDto.getStartHub())
                .destination(requestDto.getDestination())
                .deliverId(requestDto.getDeliverId())
                .deliverName(requestDto.getDeliverName())
                .deliverChannelId(requestDto.getDeliverChannelId())
                .orderDescription(requestDto.getOrderDescription())
                .departureDeadline(requestDto.getDepartureDeadline())
                .build();
    }
}