package com.owlexpress.payment.application.dto.response;

import com.owlexpress.payment.application.constant.ProductType;
import com.owlexpress.payment.domain.constant.PaymentStatus;
import com.owlexpress.payment.domain.entity.Payment;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PaymentFindResponseDto {

    private BigDecimal totalPrice;
    private String transactionId;
    private List<Order> orderProducts;
    private PaymentStatus status;
    private LocalDateTime createdAt;

    @Builder
    public PaymentFindResponseDto(
            BigDecimal totalPrice,
            String transactionId,
            List<Order> orderProducts,
            PaymentStatus status,
            LocalDateTime createdAt
    ) {
        this.totalPrice = totalPrice;
        this.transactionId = transactionId;
        this.orderProducts = orderProducts;
        this.status = status;
        this.createdAt = createdAt;
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class Order {

        private UUID productId;
        private String productName;
        private ProductType productType;
        private BigDecimal price;
        private Long quantity;

        @Builder
        public Order(
                UUID productId,
                String productName,
                BigDecimal price,
                ProductType productType,
                Long quantity
        ) {
            this.productId = productId;
            this.productName = productName;
            this.price = price;
            this.productType = productType;
            this.quantity = quantity;
        }
    }

    public void setInfo(Payment payment) {
        this.createdAt = payment.getCreatedAt();
        this.status = payment.getPaymentStatus();
        this.transactionId = payment.getTransactionId();
    }

}
