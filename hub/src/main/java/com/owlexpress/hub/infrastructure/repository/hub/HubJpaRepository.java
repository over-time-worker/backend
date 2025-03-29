package com.owlexpress.hub.infrastructure.repository.hub;

import com.owlexpress.hub.domain.entity.Hub;
import com.owlexpress.hub.domain.entity.HubProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.repository.query.Param;

public interface HubJpaRepository extends JpaRepository<Hub, UUID> {

    @Query("select h.hubProduct from Hub h inner join h.hubProduct hp on hp.hub = h where hp.hubProductId = :hubProductId")
    Optional<HubProduct> findByHubProductId(@Param("hubProductId") UUID hubProductId);

    @Query("""
            SELECT DISTINCT h
            FROM Hub h
            LEFT JOIN FETCH h.startIntervals si
            LEFT JOIN FETCH si.endHub
            """)
    List<Hub> findAllWithIntervals();

    List<Hub> findAllByParentHubIsNull();

    List<Hub> findAllByParentHub(Hub parentHub);

    @Query("""
            SELECT distinct hp
            FROM Hub h
            INNER JOIN h.hubProduct hp ON hp.hub = h
            WHERE hp.hubProductId in :productIds
            """)
    List<HubProduct> findProductsWithStock(@Param("productIds") List<UUID> productIds);

}
