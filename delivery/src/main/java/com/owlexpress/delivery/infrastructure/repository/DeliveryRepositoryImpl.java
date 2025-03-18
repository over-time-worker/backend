package com.owlexpress.delivery.infrastructure.repository;

import com.owlexpress.delivery.domain.repository.DeliveryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class DeliveryRepositoryImpl implements DeliveryRepository {
    private final DeliveryJpaRepository deliveryJpaRepository;
    private final DeliveryQueryRepository deliveryQueryRepository;
}
