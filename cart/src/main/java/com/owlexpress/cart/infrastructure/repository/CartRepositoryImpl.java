package com.owlexpress.cart.infrastructure.repository;

import com.owlexpress.cart.domain.CartRepository;
import com.owlexpress.cart.domain.entity.Cart;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CartRepositoryImpl implements CartRepository {
    private final CartJpaRepository cartJpaRepository;

    @Override
    public Cart save(Cart cart) {
        return cartJpaRepository.save(cart);
    }

    @Override
    public Optional<Cart> findByConsumerId(UUID consumerId) {
        return cartJpaRepository.findByConsumerIdAndDeletedAtIsNull(consumerId);
    }

    @Override
    public Optional<Cart> findByCartId(UUID cartId) {
        return cartJpaRepository.findByCartIdAndDeletedAtIsNull(cartId);
    }

    @Override
    public boolean existsByConsumerId(UUID consumerId) {
        return cartJpaRepository.existsByConsumerId(consumerId);
    }
}
