package com.owlexpress.hub.application.dto.response;

import com.owlexpress.hub.common.constant.ProductType;
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
    private UUID productId;
    private String productName;
    private ProductType productType;

    @Builder
    public HubProductInfoResponseDto(
            UUID hubId,
            Point location,
            UUID productId,
            String productName,
            ProductType productType
    ) {
        this.hubId = hubId;
        this.location = location;
        this.productId = productId;
        this.productName = productName;
        this.productType = productType;
    }

}
