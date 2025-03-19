package com.owlexpress.delivery.application.dtos.request;

import com.owlexpress.delivery.domain.entity.Delivery.DeliveryStatus;
import com.owlexpress.delivery.domain.entity.Delivery.OrderType;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Point;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DeliveryCreateRequestDto {
    UUID orderId;
    String productInfo;
    UUID startHubId;
    String startHubName;
    UUID destinationHubId;
    String destinationHubName;
    UUID consumerDeliverId;
    OrderType orderType;
    String shippingAddress;
    String description;
    LocalDateTime requestArrivalTime;
    UUID consumerCompanyId;
    String consumerPhoneNumber;
    String consumerName;
    Double totalEstimateDistance;
    Duration totalEstimateDurationTime;
    List<HubListDto> hubList;

    @Getter
    public static class HubListDto {
        UUID hubId;
        String hubName;
        Double distance;
        Double estimateDistance;
        Duration estimateDurationTime;
    }
}
