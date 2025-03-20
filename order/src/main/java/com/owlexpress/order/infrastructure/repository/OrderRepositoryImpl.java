package com.owlexpress.order.infrastructure.repository;

import com.owlexpress.order.domain.entity.Order;
import com.owlexpress.order.domain.repository.OrderRepository;
import com.owlexpress.order.presentation.dto.response.OrderSearchResponseDto;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepository {
    private final OrderJpaRepository orderJpaRepository;
    private final OrderQueryDSLRepository orderQueryDSLRepository;

    @Override
    public Order save(Order order) {
        return orderJpaRepository.save(order);
    }

    @Override
    public Optional<Order> findById(UUID orderId) {
        return orderJpaRepository.findByOrderIdAndDeletedAtIsNull(orderId);
    }

    @Override
    public Page<OrderSearchResponseDto> search(
            Pageable pageable,
            String startDate,
            String endDate
    ) {
        return orderQueryDSLRepository.search(pageable, startDate, endDate);
    }
}
