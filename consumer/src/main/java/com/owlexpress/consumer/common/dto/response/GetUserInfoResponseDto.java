package com.owlexpress.consumer.common.dto.response;

import com.owlexpress.consumer.common.constant.PlatformType;
import com.owlexpress.consumer.common.constant.Role;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class GetUserInfoResponseDto {
    private Long userId;
    private String username;
    private String platformId;
    private PlatformType platformType;
    private String phoneNumber;
    private Role role;
    private Boolean isPublic;

    @Builder
    public GetUserInfoResponseDto(
            Long userId,
            String username,
            String platformId,
            PlatformType platformType,
            String phoneNumber,
            Role role,
            Boolean isPublic
    ) {
        this.userId = userId;
        this.username = username;
        this.platformId = platformId;
        this.platformType = platformType;
        this.phoneNumber = phoneNumber;
        this.role = role;
        this.isPublic = isPublic;
    }
}
