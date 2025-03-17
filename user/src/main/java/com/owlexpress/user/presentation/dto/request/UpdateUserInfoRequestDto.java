package com.owlexpress.user.presentation.dto.request;

import com.owlexpress.user.domain.constant.PlatformType;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UpdateUserInfoRequestDto {
    @NotBlank(message = "[notBlank:userId]")
    private Long userId;
    @NotBlank(message = "[notBlank:platformId]")
    private String PlatformId;
    @NotBlank(message = "[notBlank:platformType]")
    private PlatformType platformType;
    @NotBlank(message = "[notBlank:phoneNumber]")
    private String phoneNumber;
    @NotBlank(message = "[notBlank:isPublic]")
    private Boolean isPublic;
}
