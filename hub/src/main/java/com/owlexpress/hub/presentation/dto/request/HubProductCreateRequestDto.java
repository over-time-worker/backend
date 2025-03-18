package com.owlexpress.hub.presentation.dto.request;

import com.owlexpress.hub.common.ProductType;
import com.owlexpress.hub.domain.entity.Hub;
import com.owlexpress.hub.domain.entity.HubProduct;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HubProductCreateRequestDto {

    @NotNull(message = "상품 아이디는 비어있을 수 없습니다.")
    private UUID productId;

    @NotNull(message = "허브 아이디는 비어있을 수 없습니다.")
    private UUID hubId;

    @NotNull(message = "생산 업체 아이디는 비어있을 수 없습니다.")
    private UUID producerId;


    @NotBlank(message = "생산 업체명은 비어있을 수 없습니다")
    private String producerName;

    @NotNull(message = "상품명은 비어있을 수 없습니다")
    private String productName;

    @NotNull(message = "상품 타입은 비어있을 수 없습니다.")
    private ProductType productType;

    @NotNull(message = "상품 재고는 비어있을 수 없습니다.")
    @Positive(message = "상품 재고는 1개 이상이여야 합니다.")
    private Long productStock;

    @Builder
    public HubProductCreateRequestDto(
            UUID productId,
            UUID hubId,
            UUID producerId,
            String producerName,
            String productName,
            ProductType productType,
            Long productStock
    ) {
        this.productId = productId;
        this.hubId = hubId;
        this.producerId = producerId;
        this.producerName = producerName;
        this.productName = productName;
        this.productType = productType;
        this.productStock = productStock;
    }

    public HubProduct toEntityWithHub(Hub hub) {
        return HubProduct.builder()
                .hub(hub)
                .producerId(this.producerId)
                .producerName(this.producerName)
                .productId(this.productId)
                .productName(this.productName)
                .productType(this.productType)
                .productStock(this.productStock)
                .build();
    }
}
