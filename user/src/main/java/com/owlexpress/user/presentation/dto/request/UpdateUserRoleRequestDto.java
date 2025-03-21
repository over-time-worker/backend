package com.owlexpress.user.presentation.dto.request;

import com.owlexpress.user.domain.constant.Role;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UpdateUserRoleRequestDto {
    private Long userId;
    private Role role;

    @Builder
    public UpdateUserRoleRequestDto(Long userId, Role role) {
        this.userId = userId;
        this.role = role;
    }
}
