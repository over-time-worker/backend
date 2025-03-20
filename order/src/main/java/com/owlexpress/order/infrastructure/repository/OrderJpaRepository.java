package com.owlexpress.order.infrastructure.repository;

import com.owlexpress.order.domain.entity.Order;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderJpaRepository extends JpaRepository<Order, UUID> {

    Optional<Order> findByOrderIdAndDeletedAtIsNull(UUID orderId);
}
