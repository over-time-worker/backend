package com.owlexpress.order.domain.entity;

import com.owlexpress.order.common.BaseEntity;
import com.owlexpress.order.common.constant.OrderStatus;
import com.owlexpress.order.common.constant.OrderType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
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
    private Long userId;

    @Column(name = "consumer_id", nullable = false)
    private UUID consumerId;

    @Column(name = "hub_id", nullable = false)
    private UUID hubId;

    @Column(name = "consumer_address", nullable = false)
    private String consumerAddress;

    @Column(name = "delivery_id")
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

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "order_id")
    private List<OrderProduct> orderProducts = new ArrayList<>();

    @Builder
    public Order(
            Long userId,
            UUID consumerId,
            UUID hubId,
            String consumerAddress,
            UUID deliveryId,
            BigDecimal totalPrice,
            String description,
            LocalDateTime requestArrivalTime,
            OrderType orderType,
            OrderStatus orderStatus,
            String productInfo
    ) {
        this.userId = userId;
        this.consumerId = consumerId;
        this.hubId = hubId;
        this.consumerAddress = consumerAddress;
        this.deliveryId = deliveryId;
        this.totalPrice = totalPrice;
        this.description = description;
        this.requestArrivalTime = requestArrivalTime;
        this.orderType = orderType;
        this.orderStatus = orderStatus;
        this.productInfo = productInfo;
    }

    public void addOrderProduct(OrderProduct orderProduct) {
        this.orderProducts.add(orderProduct);
    }

    public void deleteOrder(Long userId) {
        this.orderStatus = OrderStatus.CANCEL;
        this.orderProducts.forEach(orderProduct -> orderProduct.softDeleteData(userId));
        super.softDeleteData(userId);
    }

    public void setDeliveryId(UUID deliveryId) {
        this.deliveryId = deliveryId;
    }

    public void setDescription(String description, Long userId) {
        this.description = description;
        super.setModifiedBy(userId);
    }
}
