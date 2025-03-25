package com.owlexpress.user.common.dto;

import com.owlexpress.user.common.constant.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PassportDto {
    private Long userId;
    private Role userRole;
}