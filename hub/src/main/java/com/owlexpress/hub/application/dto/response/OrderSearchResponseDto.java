package com.owlexpress.hub.application.dto.response;


import com.owlexpress.hub.common.constant.ProductType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderSearchResponseDto {

    private UUID orderId;
    private Long userId;
    private UUID consumerId;
    private UUID hubId;
    private String consumerAddress;
    private UUID deliveryId;
    private BigDecimal totalPrice;
    private String description;
    private LocalDateTime requestArrivalTime;
    private String orderType;
    private String orderStatus;
    private List<OrderProduct> orderProducts;

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class OrderProduct {

        private UUID orderId;
        private UUID productId;
        private String productName;
        private ProductType productType;
        private Long amount;
        private BigDecimal price;

        public OrderProduct(UUID orderId, UUID productId, String productName,
                ProductType productType,
                Long amount, BigDecimal price) {
            this.orderId = orderId;
            this.productId = productId;
            this.productName = productName;
            this.productType = productType;
            this.amount = amount;
            this.price = price;
        }
    }
}
