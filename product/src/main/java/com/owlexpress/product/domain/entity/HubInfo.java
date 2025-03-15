package com.owlexpress.product.domain.entity;

import com.owlexpress.product.common.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "p_hub_info")
public class HubInfo extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID hubInfoId;

    @Column(name = "producer_id")
    private UUID producerId;

    @Column(name = "producer_name")
    @Size(min = 1, max = 50)
    private String producerName;

    @Column(name = "producer_address")
    @Size(min = 1, max = 255)
    private String producerAddress;

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
            UUID producerId,
            String producerName,
            String producerAddress,
            UUID hubId,
            Integer hubProductQuantity,
            Product product
    )
    {
        this.hubInfoId = hubInfoId;
        this.producerId = producerId;
        this.producerName = producerName;
        this.producerAddress = producerAddress;
        this.hubId = hubId;
        this.hubProductQuantity = hubProductQuantity;
        this.product = product;
    }
}
