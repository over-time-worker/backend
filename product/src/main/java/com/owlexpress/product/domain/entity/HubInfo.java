package com.owlexpress.product.domain.entity;

import com.owlexpress.product.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "p_hub_info")
public class HubInfo extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID hubInfoId;

    @Column(name = "producer_id")
    private UUID producerId;

    @Column(name = "producer_name")
    private String producerName;

    @Column(name = "producer_address")
    private String producerAddress;

    @Column(name = "hub_id")
    private UUID hubId;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
}
