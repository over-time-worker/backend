package com.owlexpress.product.domain.entity;

import com.owlexpress.product.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "p_hub_info")
@SQLRestriction("deleted_at is null")
public class HubInfo extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "hub_info_id")
    private UUID hubInfoId;

    @Column(name = "hub_id")
    private UUID hubId;

    @Column(name = "hub_product_quantity")
    private Integer hubProductQuantity;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Builder
    public HubInfo(
            UUID hubInfoId,
            UUID hubId,
            Integer hubProductQuantity,
            Product product
    )
    {
        this.hubInfoId = hubInfoId;
        this.hubId = hubId;
        this.hubProductQuantity = hubProductQuantity;
        this.product = product;
    }
}
