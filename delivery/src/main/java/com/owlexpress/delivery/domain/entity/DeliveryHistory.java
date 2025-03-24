package com.owlexpress.delivery.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.owlexpress.delivery.application.dtos.DeliveryCacheDto.DeliveryHistoryCacheDto;
import com.owlexpress.delivery.application.dtos.request.DeliveryCompleteRequestDto;
import com.owlexpress.delivery.application.dtos.request.DeliveryCreateRequestDto.HubListDto;
import com.owlexpress.delivery.application.dtos.response.AlarmCreateResponseDto;
import com.owlexpress.delivery.application.exceptions.DeliveryException.NotSupportedPlatformTypeException;
import com.owlexpress.delivery.domain.entity.Delivery.DeliveryStatus;
import io.hypersistence.utils.hibernate.type.interval.PostgreSQLIntervalType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.Type;
import org.springframework.util.StringUtils;

@Getter
@Entity
@Table(name = "p_delivery_history")
@SQLRestriction("deleted_at is null")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DeliveryHistory extends BaseEntity implements Serializable {
    @Id
    @Column(name = "delivery_history_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @JoinColumn(name = "delivery_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private Delivery delivery;

    @Column(name = "sequence", columnDefinition = "SMALLINT", nullable = false)
    private Integer sequence;

    @Column(name = "start_hub_id", length = 50)
    private UUID startHubId;

    @Column(name = "start_hub_name", length = 50)
    private String startHubName;

    @Column(name = "destination_hub_id", length = 50)
    private UUID destinationHubId;

    @Column(name = "destination_hub_name", length = 50)
    private String destinationHubName;

    @Column(name = "shipping_address", length = 50)
    private String shippingAddress;

    @Column(name = "estimate_distance")
    private Double estimateDistance;

    @Column(name = "estimate_duration_time",columnDefinition = "INTERVAL")
    @Type(PostgreSQLIntervalType.class)
    private Duration estimateDurationTime;

    @Column(name = "actual_distance")
    private Double actualDistance;

    @Column(name = "actual_time",columnDefinition = "INTERVAL")
    @Type(PostgreSQLIntervalType.class)
    private Duration actualTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "delivery_status", nullable = false)
    private DeliveryStatus deliveryStatus = DeliveryStatus.PENDING_AT_HUB;

    @Column(name = "deliver_id", length = 50)
    private UUID deliverId;

    @Column(name = "deliver_phone_number", length = 15)
    private String deliverPhoneNumber;

    @Column(name = "deliver_name", length = 50)
    private String deliverName;

    @Enumerated(EnumType.STRING)
    @Column(name = "platform_type")
    private PlatformType platformType;

    @Column(name = "deliver_channel_id", length = 50)
    private String deliverChannelId;

    @Builder
    public DeliveryHistory(
            UUID id,
            Delivery delivery,
            Integer sequence,
            UUID startHubId,
            String startHubName,
            UUID destinationHubId,
            String destinationHubName,
            String shippingAddress,
            Double estimateDistance,
            Duration estimateDurationTime,
            Double actualDistance,
            Duration actualTime,
            DeliveryStatus deliveryStatus,
            UUID deliverId,
            String deliverPhoneNumber,
            String deliverName,
            PlatformType platformType,
            String deliverChannelId
    ){
        this.id = id;
        this.delivery = delivery;
        this.sequence = sequence;
        this.startHubId = startHubId;
        this.startHubName = startHubName;
        this.destinationHubId = destinationHubId;
        this.destinationHubName = destinationHubName;
        this.shippingAddress = shippingAddress;
        this.estimateDistance = estimateDistance;
        this.estimateDurationTime = estimateDurationTime;
        this.actualDistance = actualDistance;
        this.actualTime = actualTime;
        this.deliveryStatus = deliveryStatus;
        this.deliverId = deliverId;
        this.deliverPhoneNumber = deliverPhoneNumber;
        this.deliverName = deliverName;
        this.platformType = platformType;
        this.deliverChannelId = deliverChannelId;

    }

    public static List<DeliveryHistory> createDeliveryHistoryList(Delivery delivery, List<HubListDto> hubListDtos, Long userId) {
        List<DeliveryHistory> deliveryHistoryList = new ArrayList<>();
        HubListDto lastHubListDto = hubListDtos.get(hubListDtos.size() - 1);

        for(int i = 0; i < hubListDtos.size() - 1; i++) {
            HubListDto startHub = hubListDtos.get(i);
            HubListDto endHub = hubListDtos.get(i + 1);

            DeliveryHistory deliveryHistory = DeliveryHistory.builder()
                    .delivery(delivery)
                    .sequence(i + 1)
                    .startHubId(startHub.getHubId())
                    .startHubName(startHub.getHubName())
                    .destinationHubId(endHub.getHubId())
                    .destinationHubName(endHub.getHubName())
                    .shippingAddress(delivery.getShippingAddress())
                    .deliveryStatus(DeliveryStatus.PENDING_AT_HUB)
                    .estimateDistance(startHub.getEstimateDistance())
                    .estimateDurationTime(startHub.getEstimateDurationTime())
                    .build();

            deliveryHistory.createdEntity(userId);
            deliveryHistoryList.add(deliveryHistory);
        }

        DeliveryHistory deliveryHistory = DeliveryHistory.builder()
                .delivery(delivery)
                .sequence(hubListDtos.size())
                .startHubId(lastHubListDto.getHubId())
                .startHubName(lastHubListDto.getHubName())
                .shippingAddress(delivery.getShippingAddress())
                .deliveryStatus(DeliveryStatus.PENDING_AT_HUB)
                .estimateDistance(lastHubListDto.getEstimateDistance())
                .estimateDurationTime(lastHubListDto.getEstimateDurationTime())
                .build();

        deliveryHistory.createdEntity(userId);
        deliveryHistoryList.add(deliveryHistory);

        return deliveryHistoryList;
    }

    public static DeliveryHistory toEntity(DeliveryHistoryCacheDto deliveryHistoryCacheDto) {
        return DeliveryHistory.builder()
                .id(deliveryHistoryCacheDto.getId())
                .sequence(deliveryHistoryCacheDto.getSequence())
                .startHubId(deliveryHistoryCacheDto.getStartHubId())
                .startHubName(deliveryHistoryCacheDto.getStartHubName())
                .destinationHubId(deliveryHistoryCacheDto.getDestinationHubId())
                .destinationHubName(deliveryHistoryCacheDto.getDestinationHubName())
                .shippingAddress(deliveryHistoryCacheDto.getShippingAddress())
                .deliveryStatus(deliveryHistoryCacheDto.getDeliveryStatus())
                .estimateDistance(deliveryHistoryCacheDto.getEstimateDistance())
                .estimateDurationTime(deliveryHistoryCacheDto.getEstimateDurationTime())
                .actualDistance(deliveryHistoryCacheDto.getActualDistance())
                .actualTime(deliveryHistoryCacheDto.getActualTime())
                .deliverId(deliveryHistoryCacheDto.getDeliverId())
                .deliverPhoneNumber(deliveryHistoryCacheDto.getDeliverPhoneNumber())
                .deliverName(deliveryHistoryCacheDto.getDeliverName())
                .platformType(deliveryHistoryCacheDto.getPlatformType())
                .deliverChannelId(deliveryHistoryCacheDto.getDeliverChannelId())
                .build();
    }

    public void updateDelivery(Delivery delivery) {
        this.delivery = delivery;
    }

    public void updateDeliveryStatus(DeliveryStatus deliveryStatus, Long userId) {
        this.deliveryStatus = deliveryStatus;
        this.modifiedEntity(userId);
    }

    public void updateDeliveryHistoryActualInfo(DeliveryStatus deliveryStatus, DeliveryCompleteRequestDto requestDto, Long userId) {
        this.deliveryStatus = deliveryStatus;
        this.actualDistance = requestDto.getActualDistance();
        this.actualTime = requestDto.getActualTime();
        this.modifiedEntity(userId);
    }

    public void updateDeliverInfo(AlarmCreateResponseDto alarmCreateResponseDto, Long userId) {
        this.deliverId = alarmCreateResponseDto.getDeliverId();
        this.deliverName = alarmCreateResponseDto.getDeliverName();
        this.deliverChannelId = alarmCreateResponseDto.getDeliverChannelId();
        this.deliverPhoneNumber = alarmCreateResponseDto.getDeliverPhoneNumber();
        this.modifiedEntity(userId);
    }

    @RequiredArgsConstructor
    public enum PlatformType{
        SLACK("slack");

        private final String value;

        public static PlatformType getType(String type) {
            if(!StringUtils.hasText(type)) {
                throw new NotSupportedPlatformTypeException("플랫폼 타입이 비어있습니다.");
            }

            return Arrays.stream(PlatformType.values())
                    .filter(val -> val.name().equalsIgnoreCase(type.trim()))
                    .findFirst()
                    .orElseThrow(() -> new NotSupportedPlatformTypeException("지원하지 않는 플랫폼 타입 입니다. : " + type));
        }
    }

}
