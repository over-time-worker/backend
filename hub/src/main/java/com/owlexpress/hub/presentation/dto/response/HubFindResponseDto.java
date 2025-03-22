package com.owlexpress.hub.presentation.dto.response;

import com.owlexpress.hub.domain.entity.Hub;
import lombok.*;

import java.util.UUID;

@Getter
@Builder
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

    public static HubFindResponseDto fromEntity(Hub hub) {
        return HubFindResponseDto.builder()
                .hubId(hub.getHubId())
                .name(hub.getName())
                .hubAddress(hub.getHubAddress())
                .latitude(hub.getLocation() != null ? hub.getLocation().getY() : null)
                .longitude(hub.getLocation() != null ? hub.getLocation().getX() : null)
                .managerId(hub.getUserId() != null ? hub.getUserId() : null)
                .hubManagerName(hub.getUserName() != null ? hub.getUserName() : null)
                .hubManagerPhoneNumber(hub.getUserPhoneNumber() != null ? hub.getUserPhoneNumber() : null)
                .parentHubId(hub.getParentHub() != null ? hub.getParentHub().getHubId() : null)
                .build();
    }
}
