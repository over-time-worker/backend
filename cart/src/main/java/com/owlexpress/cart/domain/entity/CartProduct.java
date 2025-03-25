package com.owlexpress.cart.domain.entity;

import com.owlexpress.cart.common.BaseEntity;
import com.owlexpress.cart.domain.entity.constant.ProductType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "p_cart_product")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@SQLRestriction("deleted_at is null")
public class CartProduct extends BaseEntity {
    @Id
    @Column(name = "cart_product_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID cartProductId;

    @JoinColumn(name = "cart_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Cart cart;

    @Column(name = "product_id")
    private UUID productId;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "product_price")
    private BigDecimal productPrice;

    @Column(name = "product_quantity")
    private Integer productQuantity;

    @Enumerated(EnumType.STRING)
    @Column(name = "product_type")
    private ProductType productType;

    @Column(name = "is_sold_out")
    private Boolean isSoldOut;

    @Builder
    public CartProduct(
            Cart cart,
            UUID productId,
            String productName,
            BigDecimal productPrice,
            Integer productQuantity,
            ProductType productType,
            Boolean isSoldOut
    ) {
        this.cart = cart;
        this.productId = productId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productQuantity = productQuantity;
        this.productType = productType;
        this.isSoldOut = isSoldOut;
    }

    public void setIsSoldOut (Boolean isSoldOut) { this.isSoldOut = isSoldOut; }

    public void deleteCartProduct(Long userId) {
        super.softDeleteData(userId);
    }

    public void increaseQuantity() {
        this.productQuantity = this.productQuantity + 1;
    }

    public boolean decreaseQuantity() {
        if (this.productQuantity == 1) return false;
        this.productQuantity = this.productQuantity - 1;
        return true;
    }
}