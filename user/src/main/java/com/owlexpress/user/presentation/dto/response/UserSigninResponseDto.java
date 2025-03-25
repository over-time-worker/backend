package com.owlexpress.user.presentation.dto.response;

import com.owlexpress.user.common.constant.Role;
import com.owlexpress.user.domain.entity.User;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class UserSigninResponseDto {
    private Long userId;
    private Role role;
    private String username;
    private String phoneNumber;

    public static UserSigninResponseDto toDto(User user) {
        return UserSigninResponseDto.builder()
                .userId(user.getUserId())
                .role(user.getRole())
                .username(user.getUsername())
                .phoneNumber(user.getPhoneNumber())
                .build();
    }
}
