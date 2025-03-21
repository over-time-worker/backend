package com.owlexpress.delivery.application.dtos.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.owlexpress.delivery.domain.entity.Delivery.OrderType;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DeliveryCreateRequestDto {
    private UUID orderId;
    private String productInfo;
    private UUID startHubId;
    private String startHubName;
    private UUID destinationHubId;
    private String destinationHubName;
    private OrderType orderType;
    private String shippingAddress;
    private String description;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime requestArrivalTime;
    private UUID consumerCompanyId;
    private String consumerPhoneNumber;
    private String consumerName;
    private Double totalEstimateDistance;
    private Duration totalEstimateDurationTime;
    private List<HubListDto> hubList;

    @Getter
    public static class HubListDto {
        private UUID hubId;
        private String hubName;
        private Double estimateDistance;
        private Duration estimateDurationTime;
    }
}
