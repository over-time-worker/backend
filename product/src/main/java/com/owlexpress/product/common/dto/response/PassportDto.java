package com.owlexpress.product.common.dto.response;

import com.owlexpress.product.common.constant.Role;
import lombok.Data;

@Data
public class PassportDto {
    private Long userId;
    private Role userRole;
}
