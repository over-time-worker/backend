package com.owlexpress.user.presentation.dto.request;

import com.owlexpress.user.common.constant.PlatformType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UpdateUserInfoRequestDto {
    @NotBlank(message = "[notBlank:userId]")
    private Long userId;
    @NotBlank(message = "[notBlank:platformId]")
    private String PlatformId;
    @NotNull(message = "[notNull:platformType]")
    private PlatformType platformType;
    @NotBlank(message = "[notBlank:phoneNumber]")
    private String phoneNumber;
    private Boolean isPublic;

    @Builder
    public UpdateUserInfoRequestDto(
            Long userId,
            String platformId,
            PlatformType platformType,
            String phoneNumber,
            Boolean isPublic
    ) {
        this.userId = userId;
        PlatformId = platformId;
        this.platformType = platformType;
        this.phoneNumber = phoneNumber;
        this.isPublic = isPublic;
    }
}
