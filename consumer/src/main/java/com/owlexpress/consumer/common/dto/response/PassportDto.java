package com.owlexpress.consumer.common.dto.response;

import com.owlexpress.consumer.common.constant.Role;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PassportDto {
    private Long userId;
    private Role userRole;

    @Builder
    public PassportDto(
            Long userId,
            Role userRole
    ) {
        this.userId = userId;
        this.userRole = userRole;
    }
}
