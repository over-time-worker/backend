package com.owlexpress.cart.domain.entity;

import com.owlexpress.cart.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "p_cart")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLRestriction("deleted_at is null")
public class Cart extends BaseEntity {
    @Id
    @Column(name = "cart_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID cartId;

    @Column(name = "consumer_id")
    private UUID consumerId;

    @Column(name = "total_price")
    private BigDecimal totalPrice;

    @Builder
    public Cart(UUID consumerId, BigDecimal totalPrice) {
        this.consumerId = consumerId;
        this.totalPrice = totalPrice;
    }
}
