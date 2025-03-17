package com.owlexpress.product.presentation.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UpdateHubInfoRequestDto {

    @NotNull(message = "hubProductQuantity는 필수 입력값입니다.")
    private int hubProductQuantity;

    @Builder
    public UpdateHubInfoRequestDto(int hubQuantity) {
        this.hubProductQuantity = hubQuantity;
    }
}
