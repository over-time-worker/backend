package com.owlexpress.producer.common.dto.response;

import com.owlexpress.producer.common.constant.Role;
import lombok.*;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PassportDto {
    private Long userId;
    private Role userRole;
}
