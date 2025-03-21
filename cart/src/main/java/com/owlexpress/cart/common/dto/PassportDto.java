package com.owlexpress.cart.common.dto;

import com.owlexpress.cart.common.constant.Role;
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