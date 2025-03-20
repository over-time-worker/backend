package com.owlexpress.hub.presentation.dto.request;

import com.owlexpress.hub.common.util.GeoUtil;
import com.owlexpress.hub.domain.entity.Hub;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class HubUpdateRequestDto {

    // TODO: 추후 PATCH 요청으로 받는 필드만 수정 고려
    private UUID hubId;

    private String name;

    private String hubAddress;

    private Double latitude;

    private Double longitude;

    private Long userId;

    private String userName;

    @Pattern(regexp = "^01(?:0|1|[6-9])-(?:\\d{3}|\\d{4})-\\d{4}$", message = "전화번호 형식이 일치하지 않습니다.")
    private String userPhoneNumber;

    private Hub parentHub;


    public Hub toEntity() {
        return Hub.builder()
                .name(this.name)
                .hubAddress(this.hubAddress)
                .location(GeoUtil.createPoint(this.latitude, this.longitude))
                .userId(this.userId)
                .userName(this.userName)
                .userPhoneNumber(this.userPhoneNumber)
                .parentHub(this.parentHub)
                .build();
    }
}
