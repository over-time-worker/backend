package com.owlexpress.hub.infrastructure.repository;

import com.owlexpress.hub.domain.entity.Hub;
import com.owlexpress.hub.domain.entity.HubProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface HubJpaRepository extends JpaRepository<Hub, UUID> {

    @Query("select h.hubProduct from Hub h inner join h.hubProduct hp on hp.hub = h where hp.hubProductId = :hubProductId")
    Optional<HubProduct> findByHubProductId(UUID hubProductId);

    @Query("""
    SELECT DISTINCT h 
    FROM Hub h
    LEFT JOIN FETCH h.startIntervals si
    LEFT JOIN FETCH si.endHub
    """)    List<Hub> findAllWithIntervals();

    List<Hub> findAllByParentHubIsNull();

    List<Hub> findAllByParentHub(Hub parentHub);
}
