package com.owlexpress.payment.common;

import com.owlexpress.payment.common.constant.Role;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PassportDto {

    private Long userId;
    private Role userRole;

    @Builder
    public PassportDto(Long userId, Role userRole) {
        this.userId = userId;
        this.userRole = userRole;
    }
}
