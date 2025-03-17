package com.owlexpress.user.presentation.dto.response;

import com.owlexpress.user.domain.constant.ApprovalStatus;
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
public class GetUsersResponseDto {
    private Long userId;
    private String accountId;
    private String username;
    private String phoneNumber;
    private String platformId;
    private PlatformType platformType;
    private Role role;
    private ApprovalStatus approvalStatus;
    private Boolean isPublic;
}
