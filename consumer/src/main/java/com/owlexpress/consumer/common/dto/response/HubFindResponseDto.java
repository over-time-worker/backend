package com.owlexpress.consumer.common.dto.response;

import lombok.*;

import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HubFindResponseDto {

    private UUID hubId;

    private String name;

    private String hubAddress;

    private Double latitude;
    private Double longitude;

    private Long managerId;

    private String hubManagerName;
    private String hubManagerPhoneNumber;
    private UUID parentHubId;

    @Builder
    public HubFindResponseDto(
            UUID hubId,
            String name,
            String hubAddress,
            Double latitude,
            Double longitude,
            Long managerId,
            String hubManagerName,
            String hubManagerPhoneNumber,
            UUID parentHubId
    ) {
        this.hubId = hubId;
        this.name = name;
        this.hubAddress = hubAddress;
        this.latitude = latitude;
        this.longitude = longitude;
        this.managerId = managerId;
        this.hubManagerName = hubManagerName;
        this.hubManagerPhoneNumber = hubManagerPhoneNumber;
        this.parentHubId = parentHubId;
    }
}
