package com.owlexpress.hub.presentation.dto;

import com.owlexpress.hub.common.util.GeoUtil;
import com.owlexpress.hub.domain.entity.Hub;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class HubCreateRequestDto {

    @NotBlank(message = "이름은 비어있을 수 없습니다.")
    private String name;

    @NotNull(message = "허브 주소는 비어있을 수 없습니다.")
    private String hubAddress;

    @NotNull(message = "경도 값은 비어있을 수 없습니다.")
    private Double latitude;

    @NotNull(message = "위도 값은 비어있을 수 없습니다.")
    private Double longitude;

    @NotNull(message = "유저 ID는 비어있을 수 없습니다.")
    private Long userId;

    private UUID parentId;

    public Hub toEntity() {
        return Hub.builder()
                .name(this.name)
                .hubAddress(this.hubAddress)
                .location(GeoUtil.createPoint(this.latitude, this.longitude))
                .userId(this.userId)
                .parentHubId(this.parentId)
                .build();
    }
}
