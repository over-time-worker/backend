package com.owlexpress.hub.presentation.dto.request;

import com.owlexpress.hub.common.util.GeoUtil;
import com.owlexpress.hub.domain.entity.Hub;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

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

    @NotBlank(message = "유저 이름은 비어있을 수 없습니다.")
    private String userName;

    @Pattern(regexp = "^01(?:0|1|[6-9])-(?:\\d{3}|\\d{4})-\\d{4}$", message = "전화번호 형식이 일치하지 않습니다.")
    private String userPhoneNumber;


    private UUID parentId;


    public Hub toEntity() {
        return Hub.builder()
                .name(this.name)
                .hubAddress(this.hubAddress)
                .location(GeoUtil.createPoint(this.latitude, this.longitude))
                .userId(this.userId)
                .userName(this.userName)
                .userPhoneNumber(this.userPhoneNumber)
                .parentHubId(this.parentId)
                .build();
    }
}
