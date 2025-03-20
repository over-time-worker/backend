package com.owlexpress.delivery.infrastructure.repository;

import com.owlexpress.delivery.application.dtos.response.DeliveryFindResponseDto;
import com.owlexpress.delivery.domain.entity.Delivery;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface DeliveryQueryRepository {

    Optional<Delivery> findDeliveryByIdWithDeliveryHistories(UUID id);

    Page<DeliveryFindResponseDto> search(Pageable pageable, String startDate, String endDate, UUID deliveryId, String deliveryStatus);
}
