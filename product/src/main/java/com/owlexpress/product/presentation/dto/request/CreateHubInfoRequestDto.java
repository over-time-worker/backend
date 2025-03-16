package com.owlexpress.product.presentation.dto.request;

import com.owlexpress.product.domain.entity.HubInfo;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
public class CreateHubInfoRequestDto {

    @NotNull(message = "hubId는 필수 입력값입니다.")
    private UUID hubId;
    @NotNull(message = "productId는 필수 입력값입니다.")
    private UUID productId;
    @NotNull(message = "hubProductQuantity는 필수 입력값입니다.")
    private Integer hubProductQuantity;

    @Builder
    public CreateHubInfoRequestDto(
            UUID hubId,
            UUID productId,
            Integer hubProductQuantity
    )
    {
        this.hubId = hubId;
        this.productId = productId;
        this.hubProductQuantity = hubProductQuantity;
    }

    public static HubInfo toEntity(CreateHubInfoRequestDto createHubInfoRequestDto) {
        return HubInfo.builder()
                .product(null)
                .hubId(createHubInfoRequestDto.getHubId())
                .hubProductQuantity(createHubInfoRequestDto.getHubProductQuantity())
                .build();
    }
}
