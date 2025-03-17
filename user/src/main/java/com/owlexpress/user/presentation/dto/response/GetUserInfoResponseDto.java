package com.owlexpress.user.presentation.dto.response;

import com.owlexpress.user.domain.constant.PlatformType;
import com.owlexpress.user.domain.constant.Role;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Builder
public class GetUserInfoResponseDto {
    private Long userId;
    private String username;
    private String platformId;
    private PlatformType platformType;
    private String phoneNumber;
    private Role role;
    private Boolean isPublic;
}
