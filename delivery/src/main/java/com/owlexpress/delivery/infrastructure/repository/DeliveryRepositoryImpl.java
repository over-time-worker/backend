package com.owlexpress.delivery.infrastructure.repository;

import com.owlexpress.delivery.application.dtos.response.DeliveryFindResponseDto;
import com.owlexpress.delivery.domain.entity.Delivery;
import com.owlexpress.delivery.domain.repository.DeliveryRepository;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class DeliveryRepositoryImpl implements DeliveryRepository {
    private final DeliveryJpaRepository deliveryJpaRepository;
    private final DeliveryQueryRepository deliveryQueryRepository;

    @Override
    public Optional<Delivery> findById(UUID id) {
        return deliveryJpaRepository.findById(id);
    }

    @Override
    public Optional<Delivery> findDeliveryByIdWithDeliveryHistories(UUID id) {
        return deliveryQueryRepository.findDeliveryByIdWithDeliveryHistories(id);
    }

    @Override
    public Page<DeliveryFindResponseDto> search(
            Pageable pageable,
            String startDate,
            String endDate,
            UUID deliveryId,
            String deliveryStatus
    ) {
        return deliveryQueryRepository.search(pageable, startDate, endDate, deliveryId, deliveryStatus);
    }

    @Override
    public Delivery save(Delivery delivery) {
        return deliveryJpaRepository.save(delivery);
    }
}
