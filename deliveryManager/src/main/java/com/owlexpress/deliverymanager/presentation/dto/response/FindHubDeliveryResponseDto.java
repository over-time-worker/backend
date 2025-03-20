package com.owlexpress.deliverymanager.presentation.dto.response;

import com.owlexpress.deliverymanager.domain.constant.PlatformType;
import com.owlexpress.deliverymanager.domain.entity.HubDeliveryManager;
import lombok.Builder;
import lombok.Data;

@Data
public class FindHubDeliveryResponseDto {

    private Long userId;
    private Integer assign_number;
    private String user_name;
    private String user_phone_number;
    private PlatformType platform_type;
    private Long channelId;

    @Builder
    public FindHubDeliveryResponseDto(
            Long userId,
            Integer assign_number,
            String user_name,
            String user_phone_number,
            PlatformType platform_type,
            Long channelId
    ) {
        this.userId = userId;
        this.assign_number = assign_number;
        this.user_name = user_name;
        this.user_phone_number = user_phone_number;
        this.platform_type = platform_type;
        this.channelId = channelId;
    }

    public static FindHubDeliveryResponseDto fromEntity(HubDeliveryManager hubDeliveryManager) {
        return FindHubDeliveryResponseDto.builder()
                                  .userId(hubDeliveryManager.getUserId())
                                  .assign_number(hubDeliveryManager.getAssignNumber())
                                  .user_name(hubDeliveryManager.getUserName())
                                  .user_phone_number(hubDeliveryManager.getUserPhoneNumber())
                                  .platform_type(hubDeliveryManager.getPlatformType())
                                  .channelId(hubDeliveryManager.getChannelId())
                                  .build();
    }
}
