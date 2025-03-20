package com.owlexpress.deliverymanager.presentation.dto.request;

import com.owlexpress.deliverymanager.domain.constant.PlatformType;
import com.owlexpress.deliverymanager.domain.entity.HubDeliveryManager;
import lombok.Builder;
import lombok.Data;

@Data
public class CreateHubDeliveryManagerRequestDto {
    private Long userId;
    private Integer assign_number;
    private String user_name;
    private String user_phone_number;
    private PlatformType platform_type;
    private Long channelId;

    @Builder
    public CreateHubDeliveryManagerRequestDto(
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

    public HubDeliveryManager toEntity(CreateHubDeliveryManagerRequestDto createConsumerDeliveryManagerRequestDto,
                                       int newAssignNumber
    ) {
        return HubDeliveryManager.builder()
                          .userId(createConsumerDeliveryManagerRequestDto.getUserId())
                          .assignNumber(newAssignNumber)
                          .userName(createConsumerDeliveryManagerRequestDto.getUser_name())
                          .userPhoneNumber(createConsumerDeliveryManagerRequestDto.getUser_phone_number())
                          .platformType(createConsumerDeliveryManagerRequestDto.getPlatform_type())
                          .channelId(createConsumerDeliveryManagerRequestDto.channelId)
                          .build();

    }
}
