package com.owlexpress.payment.domain.entity;

import com.owlexpress.payment.common.BaseEntity;
import com.owlexpress.payment.domain.constant.PaymentStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "p_payment")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Payment extends BaseEntity {

    @Id
    @Column(name = "payment_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID paymentId;

    @Column(name = "order_id", nullable = false)
    private UUID orderId;

    @Column(name = "transaction_id", nullable = false)
    private UUID transactionId;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", nullable = false)
    private PaymentStatus paymentStatus;


    @Builder
    public Payment(UUID paymentId, UUID orderId, UUID transactionId, BigDecimal price,
            PaymentStatus paymentStatus) {
        this.paymentId = paymentId;
        this.orderId = orderId;
        this.transactionId = transactionId;
        this.price = price;
        this.paymentStatus = paymentStatus;
    }
}
