package com.owlexpress.user.presentation.dto.request;

import com.owlexpress.user.domain.constant.PlatformType;
import com.owlexpress.user.domain.constant.Role;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
public class UserSignupRequestDto {
    @NotBlank(message = "[notBlank:accountId]")
    private String accountId;
    @NotBlank(message = "[notBlank:password]")
    private String password;
    @NotBlank(message = "[notBlank:username]")
    private String username;
    @NotBlank(message = "[notBlank:phoneNumber]")
    private String phoneNumber;
    @NotBlank(message = "[notBlank:platformId]")
    private String platformId;
    @NotBlank(message = "[notBlank:platformType]")
    private PlatformType platformType;
    @NotBlank(message = "[notBlank:role]")
    private Role role;
    @NotBlank(message = "[notBlank:isPublic]")
    private Boolean isPublic;
}
