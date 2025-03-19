package com.owlexpress.cart.infrastructure.repository;

import com.owlexpress.cart.domain.entity.Cart;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartJpaRepository extends JpaRepository<Cart, UUID> {

    Optional<Cart> findByConsumerIdAndDeletedAtIsNull(UUID consumerId);

    Optional<Cart> findByCartIdAndDeletedAtIsNull(UUID cartId);

    boolean existsByConsumerId(UUID consumerId);
}
