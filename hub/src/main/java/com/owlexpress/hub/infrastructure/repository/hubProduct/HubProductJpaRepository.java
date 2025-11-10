package com.owlexpress.hub.infrastructure.repository.hubProduct;

import com.owlexpress.hub.domain.entity.HubProduct;
import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface HubProductJpaRepository extends JpaRepository<HubProduct, UUID> {
    Optional<HubProduct> findByProductId(UUID productId);

    List<HubProduct> findAllByHubProductIdIn(List<UUID> hubProductIds);

    @Modifying(clearAutomatically = true)
    @Query("update HubProduct hp set hp.productStock = hp.productStock - :quantity where hp.hubProductId = :hubProductId")
    int decreaseStock(int quantity, UUID hubProductId);
}
