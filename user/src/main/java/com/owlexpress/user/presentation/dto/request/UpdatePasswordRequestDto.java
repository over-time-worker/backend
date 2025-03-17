package com.owlexpress.user.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UpdatePasswordRequestDto {
    @NotBlank(message = "[notBlank:oldPassword]")
    private String oldPassword;
    @NotBlank(message = "[notBlank:newPassword]")
    private String newPassword;
    @NotBlank(message = "[notBlank:confirmPassword]")
    private String confirmPassword;
}
