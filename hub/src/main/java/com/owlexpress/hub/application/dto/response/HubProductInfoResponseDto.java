package com.owlexpress.hub.application.dto.response;

import com.owlexpress.hub.common.constant.ProductType;
import com.owlexpress.hub.domain.entity.HubProduct;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Point;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HubProductInfoResponseDto {

    private UUID hubId;
    private Point location;
    private UUID hubProductId;
    private UUID productId;
    private String productName;
    private Long productStock;
    private ProductType productType;

    @Builder
    public HubProductInfoResponseDto(
            UUID hubId,
            Point location,
            UUID hubProductId,
            UUID productId,
            String productName,
            Long productStock,
            ProductType productType
    ) {
        this.hubId = hubId;
        this.location = location;
        this.hubProductId = hubProductId;
        this.productId = productId;
        this.productName = productName;
        this.productStock = productStock;
        this.productType = productType;
    }
}
