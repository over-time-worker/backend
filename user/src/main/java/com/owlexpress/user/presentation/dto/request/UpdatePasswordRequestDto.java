package com.owlexpress.user.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Builder;
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

    @Builder
    public UpdatePasswordRequestDto(String oldPassword, String newPassword, String confirmPassword) {
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
        this.confirmPassword = confirmPassword;
    }
}
