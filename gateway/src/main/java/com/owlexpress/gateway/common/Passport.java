package com.owlexpress.gateway.common;

import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Passport {

    private Long userId;
    private Role userRole;

    @Builder
    public Passport(Long userId, Role userRole) {
        this.userId = userId;
        this.userRole = userRole;
    }
}
