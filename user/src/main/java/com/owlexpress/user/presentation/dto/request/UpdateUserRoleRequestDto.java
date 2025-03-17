package com.owlexpress.user.presentation.dto.request;

import com.owlexpress.user.domain.constant.Role;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UpdateUserRoleRequestDto {
    private Long userId;
    private Role role;
}
