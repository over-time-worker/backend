package com.owl_express.alarm.application.dtos.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AlarmCreateRequestDto {
    @NotNull(message = "[notNull:deliverId]")
    private UUID deliverId;
    @NotNull(message = "[notNull:userId]")
    private Long deliverUserId;
    @Size(min = 1, max = 50, message = "[size:deliverName]")
    private String deliverName;
    @Size(min = 1, max = 50, message = "[size:deliverPlatformId]")
    private String deliverChannelId;
    @Size(min = 1, max = 50, message = "[size:platformName]")
    private String platformName;
    @NotNull(message = "[notNull:deliveryId]")
    private UUID deliveryId;
    @NotNull(message = "[notNull:orderId]")
    private UUID orderId;
    @NotBlank(message = "[notBlank:productInfo]")
    private String productInfo;
    @Size(min = 1, max = 50, message = "[size:ordererName]")
    private String ordererName;
    @Size(min = 1, max = 50, message = "[size:startHubName]")
    private String startHubName;
    @Size(min = 1, max = 50, message = "[size:endHubName]")
    private String endHubName;
    @Size(min = 1, max = 50, message = "[size:currentHubId]")
    private UUID currentHubId;
    @Size(min = 1, max = 50, message = "[size:currentHubName]")
    private String currentHubName;
    @Size(min = 1, max = 50, message = "[size:nextHubId]")
    private UUID nextHubId;
    @Size(min = 1, max = 50, message = "[size:nextHubName]")
    private String nextHubName;
    private String orderDescription;
    @NotNull(message = "[notNull:shippingAddress]")
    private String shippingAddress;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime requestArrivalTime;
    @NotNull(message = "[notNull:totalEstimateDistance]")
    private Double totalEstimateDistance;
    @NotNull(message = "[notNull:totalEstimateDurationTime]")
    private Duration totalEstimateDurationTime;
    @NotNull(message = "[notNull:totalHubList]")
    List<HubListDto> totalHubList;

    @Getter
    public static class HubListDto {

        UUID hubId;
        String hubName;
        Double estimateDistance;
        Duration estimateDurationTime;

    }

}
