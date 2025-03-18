package com.owlexpress.hub.domain.entity;

import com.owlexpress.hub.common.BaseEntity;
import com.owlexpress.hub.common.ProductType;
import com.owlexpress.hub.presentation.dto.request.HubProductUpdateRequestDto;
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
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Getter
@Table(name = "p_hub_product")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLRestriction("deleted_at is null")
public class HubProduct extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "hub_product_id")
    private UUID hubProductId;

    @JoinColumn(name = "hub_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Hub hub;

    @Column(name = "producer_id", nullable = false)
    private UUID producerId;

    @Column(name = "producer_name", nullable = false, length = 50)
    private String producerName;

    @Column(name = "product_name", nullable = false, length = 50)
    private String productName;

    @Column(name = "product_type", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private ProductType productType;

    @Column(name = "product_stock", nullable = false)
    private Long productStock;

    @Builder

    public HubProduct(
            UUID hubProductId,
            Hub hub,
            UUID producerId,
            String producerName,
            String productName,
            ProductType productType,
            Long productStock
    ) {
        this.hubProductId = hubProductId;
        this.hub = hub;
        this.producerId = producerId;
        this.producerName = producerName;
        this.productName = productName;
        this.productType = productType;
        this.productStock = productStock;
    }

    public void updateEntity(HubProductUpdateRequestDto requestDto) {
        this.hubProductId = requestDto.getHubProductId();
        this.productName = requestDto.getHubProductName();
        this.productStock = requestDto.getHubProductStock();
        this.productType = requestDto.getHubProductType();
    }
}
