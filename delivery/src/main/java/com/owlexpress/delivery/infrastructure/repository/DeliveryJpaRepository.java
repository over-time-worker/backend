package com.owlexpress.delivery.infrastructure.repository;

import com.owlexpress.delivery.domain.entity.Delivery;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeliveryJpaRepository extends JpaRepository<Delivery, UUID> {

}
