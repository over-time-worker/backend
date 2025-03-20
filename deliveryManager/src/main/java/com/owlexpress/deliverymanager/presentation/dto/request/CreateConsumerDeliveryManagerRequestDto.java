package com.owlexpress.deliverymanager.presentation.dto.request;

import com.owlexpress.deliverymanager.domain.PlatformType;
import com.owlexpress.deliverymanager.domain.entity.ConsumerDeliveryManager;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
public class CreateConsumerDeliveryManagerRequestDto {
    private Long userId;
    private Integer assign_number;
    private String user_name;
    private String user_phone_number;
    private PlatformType platform_type;
    private Long channelId;
    private UUID hubId;

    @Builder
    public CreateConsumerDeliveryManagerRequestDto(
            Long userId,
            Integer assign_number,
            String user_name,
            String user_phone_number,
            PlatformType platform_type,
            Long channelId,
            UUID hubId
    ) {
        this.userId = userId;
        this.assign_number = assign_number;
        this.user_name = user_name;
        this.user_phone_number = user_phone_number;
        this.platform_type = platform_type;
        this.channelId = channelId;
        this.hubId = hubId;
    }

    public ConsumerDeliveryManager toEntity(CreateConsumerDeliveryManagerRequestDto createConsumerDeliveryManagerRequestDto) {
        return ConsumerDeliveryManager.builder()
                .userId(createConsumerDeliveryManagerRequestDto.getUserId())
                .assignNumber(createConsumerDeliveryManagerRequestDto.getAssign_number())
                .userName(createConsumerDeliveryManagerRequestDto.getUser_name())
                               .userPhoneNumber(createConsumerDeliveryManagerRequestDto.getUser_phone_number())
                .platformType(createConsumerDeliveryManagerRequestDto.getPlatform_type())
                .channelId(createConsumerDeliveryManagerRequestDto.channelId)
                .build();

    }
}
