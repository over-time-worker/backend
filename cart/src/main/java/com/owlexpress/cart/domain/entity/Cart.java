package com.owlexpress.cart.domain.entity;

import static com.owlexpress.cart.common.exception.ExceptionMessage.CART_PRODUCT_NOT_FOUND_EXCEPTION_MESSAGE;

import com.owlexpress.cart.common.BaseEntity;
import com.owlexpress.cart.domain.exception.CartProductNotFoundException;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "p_cart")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLRestriction("deleted_at is null")
public class Cart extends BaseEntity {
    @Id
    @Column(name = "cart_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID cartId;

    @Column(name = "consumer_id")
    private UUID consumerId;

    @Column(name = "total_price")
    private BigDecimal totalPrice;

    @OneToMany(mappedBy = "cart",  cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartProduct> cartProduct = new ArrayList<>();

    @Builder
    public Cart(UUID consumerId, BigDecimal totalPrice) {
        this.consumerId = consumerId;
        this.totalPrice = totalPrice;
    }

    public void removeCart (Long userId){
        super.softDeleteData(userId);
    }

    // CartProduct 구현 로직

    public List<CartProduct> getCartProductList() {
        return Collections.unmodifiableList(cartProduct); // 읽기 전용으로 반환
    }

    public void addCartProduct(CartProduct cartProduct) {
        this.cartProduct.add(cartProduct);
        this.totalPrice = this.totalPrice.add(cartProduct.getProductPrice());
    }

    public void increaseCartProductQuantity (UUID CartProductId, Long userId) {
        CartProduct findCartProduct = findCartProduct(CartProductId);

        findCartProduct.increaseQuantity();

        this.totalPrice = this.totalPrice.add(findCartProduct.getProductPrice());

        super.updateModifiedData(userId);
    }

    public void decreaseCartProductQuantity (UUID cartProductId, Long userId) {
        CartProduct findCartProduct = findCartProduct(cartProductId);

        if (findCartProduct.decreaseQuantity()){
            this.totalPrice = this.totalPrice.subtract(findCartProduct.getProductPrice());
        }

        super.updateModifiedData(userId);
    }

    public void removeCartProduct(UUID cartProductId, Long userId) {
        findCartProduct(cartProductId).deleteCartProduct(userId);
    }

    private CartProduct findCartProduct(UUID cartProductId) {
        return cartProduct.stream()
                .filter(
                        cartProduct -> cartProduct
                        .getCartProductId()
                        .equals(cartProductId)
                )
                .findFirst()
                .orElseThrow(() -> new CartProductNotFoundException(
                        CART_PRODUCT_NOT_FOUND_EXCEPTION_MESSAGE
                ));
    }
}
