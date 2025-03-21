package com.owlexpress.hub.common.dto.response;

import com.owlexpress.hub.common.constant.Role;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
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
