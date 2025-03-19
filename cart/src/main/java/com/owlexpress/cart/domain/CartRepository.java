package com.owlexpress.cart.domain;

import com.owlexpress.cart.domain.entity.Cart;
import java.util.Optional;
import java.util.UUID;

public interface CartRepository {

    Cart save(Cart cart);

    Optional<Cart> findByConsumerId(UUID consumerId);

    Optional<Cart> findByCartId(UUID cartId);

}
