package com.owlexpress.order.domain.repository;

import com.owlexpress.order.domain.entity.Order;
import com.owlexpress.order.presentation.dto.response.OrderSearchResponseDto;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderRepository {

    Order save(Order order);

    Optional<Order> findById(UUID orderId);

    Page<OrderSearchResponseDto> search(
            Pageable pageable,
            String startDate,
            String endDate
    );
}
