package com.owlexpress.product.domain.entity;

import com.owlexpress.product.common.BaseEntity;
import com.owlexpress.product.domain.constant.ProductType;
import jakarta.persistence.*;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "p_product")
@SQLRestriction("deleted_at is null")
public class Product extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "product_id", nullable = false)
    private UUID productId;

    @Size(max = 50)
    @Column(name = "product_name", nullable = false)
    private String productName;

    @Digits(integer = 13, fraction = 2)
    @Column(name = "product_price", nullable = false)
    private BigDecimal productPrice;

    @Enumerated(EnumType.STRING)
    @Column(name = "product_type", nullable = false)
    private ProductType productType;

    @Column(name = "producer_id")
    private UUID producerId;

    @Size(min = 1, max = 50)
    @Column(name = "producer_name")
    private String producerName;

    @Size(min = 1, max = 255)
    @Column(name = "producer_address")
    private String producerAddress;

    @OneToMany(mappedBy = "product")
    public Set<HubInfo> hubInfo;

    @Builder
    public Product(
            String productName,
            BigDecimal productPrice,
            ProductType productType,
            UUID producerId,
            String producerName,
            String producerAddress,
            Set<HubInfo> hubInfo
    )
    {
        this.productName = productName;
        this.productPrice = productPrice;
        this.productType = productType;
        this.producerId = producerId;
        this.producerName = producerName;
        this.producerAddress = producerAddress;
        this.hubInfo = hubInfo;
    }

}
