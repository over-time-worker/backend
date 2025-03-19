package com.owlexpress.hub.presentation.dto.response;

import com.owlexpress.hub.domain.entity.Hub;
import lombok.*;

import java.util.UUID;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class HubSearchResponseDto {

    private UUID hubId;

    private String name;

    private String hubAddress;

    private Double latitude;
    private Double longitude;

    private Long managerId;

    private String hubManagerName;
    private String hubManagerPhoneNumber;
    private UUID parentHubId;

    public static HubSearchResponseDto fromEntity(Hub hub) {
        return HubSearchResponseDto.builder()
                .hubId(hub.getHubId())
                .name(hub.getName())
                .hubAddress(hub.getHubAddress())
                .latitude(hub.getLocation().getY())
                .longitude(hub.getLocation().getX())
                .managerId(hub.getUserId())
                .hubManagerName(hub.getUserName())
                .hubManagerPhoneNumber(hub.getUserPhoneNumber())
                .parentHubId(hub.getParentHubId())
                .build();
    }
}
