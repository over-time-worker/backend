package com.owlexpress.order.infrastructure.repository;

import com.owlexpress.order.presentation.dto.response.OrderSearchResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderQueryDSLRepository {
    Page<OrderSearchResponseDto> search(Pageable pageable, String startDate, String endDate);
}
