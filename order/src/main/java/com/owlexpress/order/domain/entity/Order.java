package com.owlexpress.order.domain.entity;

import com.owlexpress.order.common.entity.BaseEntity;
import com.owlexpress.order.domain.constant.OrderStatus;
import com.owlexpress.order.domain.constant.OrderType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "p_order")
public class Order extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "order_id")
    private UUID orderId;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "consumer_id", nullable = false)
    private UUID consumerId;

    @Column(name = "hub_id", nullable = false)
    private UUID hubId;

    @Column(name = "consumer_address", nullable = false)
    private String consumerAddress;

    @Column(name = "delivery_id", nullable = false)
    private UUID deliveryId;

    @Column(name = "total_price", nullable = false)
    private BigDecimal totalPrice;

    @Column(name = "description")
    private String description;

    @Column(name = "request_arrival_time")
    private LocalDateTime requestArrivalTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_type", nullable = false)
    private OrderType orderType;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_status", nullable = false)
    private OrderStatus orderStatus;

    @Column(name = "product_info")
    private String productInfo;

}
