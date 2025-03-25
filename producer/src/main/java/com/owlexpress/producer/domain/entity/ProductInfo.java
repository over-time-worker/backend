package com.owlexpress.producer.domain.entity;

import com.owlexpress.producer.common.BaseEntity;
import com.owlexpress.producer.common.constant.ProductType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "p_product_info")
@SQLRestriction("deleted_at is null")
public class ProductInfo extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "product_info_id")
    private UUID productInfoId;

    @Column(name = "product_id")
    private UUID productId;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "product_price")
    private BigDecimal productPrice;

    @Column(name = "product_type")
    @Enumerated(EnumType.STRING)
    private ProductType productType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producer_id")
    private Producer producer;

    @Builder
    public ProductInfo(
            UUID productInfoId,
            UUID productId,
            String productName,
            BigDecimal productPrice,
            ProductType productType,
            Producer producer
    ) {
        this.productInfoId = productInfoId;
        this.productId = productId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productType = productType;
        this.producer = producer;
    }
}

