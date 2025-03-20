package com.owlexpress.deliverymanager.presentation.dto.response;

import com.owlexpress.deliverymanager.domain.constant.PlatformType;
import com.owlexpress.deliverymanager.domain.entity.ConsumerDeliveryManager;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
public class FindConsumerResponseDto {

    private Long userId;
    private Integer assign_number;
    private String user_name;
    private String user_phone_number;
    private PlatformType platform_type;
    private Long channelId;
    private UUID hubId;

    @Builder
    public FindConsumerResponseDto(
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

    public static FindConsumerResponseDto fromEntity(ConsumerDeliveryManager consumerDeliveryManager) {
        return FindConsumerResponseDto.builder()
                               .userId(consumerDeliveryManager.getUserId())
                               .assign_number(consumerDeliveryManager.getAssignNumber())
                               .user_name(consumerDeliveryManager.getUserName())
                               .user_phone_number(consumerDeliveryManager.getUserPhoneNumber())
                               .platform_type(consumerDeliveryManager.getPlatformType())
                               .channelId(consumerDeliveryManager.getChannelId())
                               .hubId(consumerDeliveryManager.getHubId())
                               .build();
    }
}
