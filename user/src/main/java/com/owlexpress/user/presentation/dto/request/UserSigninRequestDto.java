package com.owlexpress.user.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserSigninRequestDto {
    @NotBlank(message = "[notBlank:accountId]")
    private String accountId;
    @NotBlank(message = "[notBlank:password]")
    private String password;
}
