package com.owlexpress.user.presentation.dto.request;

import com.owlexpress.user.domain.constant.PlatformType;
import com.owlexpress.user.domain.constant.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
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
    @NotNull(message = "[notNull:platformType]")
    private PlatformType platformType;
    @NotNull(message = "[notNull:role]")
    private Role role;
    private Boolean isPublic;

    @Builder
    public UserSignupRequestDto(
            String accountId,
            String password,
            String username,
            String phoneNumber,
            String platformId,
            PlatformType platformType,
            Role role,
            Boolean isPublic
    ) {
        this.accountId = accountId;
        this.password = password;
        this.username = username;
        this.phoneNumber = phoneNumber;
        this.platformId = platformId;
        this.platformType = platformType;
        this.role = role;
        this.isPublic = isPublic;
    }
}
