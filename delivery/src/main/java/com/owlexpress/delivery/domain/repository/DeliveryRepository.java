package com.owlexpress.delivery.domain.repository;

import com.owlexpress.delivery.common.dto.response.DeliveryFindResponseDto;
import com.owlexpress.delivery.domain.entity.Delivery;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DeliveryRepository {
    Optional<Delivery> findById(UUID id);

    Optional<Delivery> findDeliveryByIdWithDeliveryHistories(UUID id);

    Page<DeliveryFindResponseDto> search(Pageable pageable, String startDate, String endDate, UUID deliveryId, String deliveryStatus);

    Delivery save(Delivery delivery);

    Optional<Delivery> findByConsumerCompanyId(UUID consumerId);

    Boolean isExistByConsumerCompanyId(UUID consumerId);
}
