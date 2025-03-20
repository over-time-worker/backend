package com.owlexpress.order.application.service;

import com.owlexpress.order.presentation.dto.request.CreateOrderRequestDto;
import com.owlexpress.order.presentation.dto.request.UpdateOrderRequestDto;
import com.owlexpress.order.presentation.dto.request.UpdateOrderStatusRequestDto;
import com.owlexpress.order.presentation.dto.response.CreateOrderResponseDto;
import com.owlexpress.order.presentation.dto.response.GetOrderResponseDto;
import com.owlexpress.order.presentation.dto.response.OrderSearchResponseDto;
import java.util.UUID;
import org.springframework.data.web.PagedModel;

public interface OrderService {
    CreateOrderResponseDto createOrder(Long userId ,CreateOrderRequestDto request);

    GetOrderResponseDto findOrder(UUID orderId);

    void updateOrderStatus(UUID orderId,UpdateOrderStatusRequestDto request);

    void updateOrder(UUID orderId, UpdateOrderRequestDto request, Long userId);

    void deleteOrder(UUID orderId, Long userId);

    PagedModel<OrderSearchResponseDto> search (
            int page,
            int size,
            String sort,
            String orderBy,
            String startDate,
            String endDate
    );
}
