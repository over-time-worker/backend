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
    CreateOrderResponseDto createOrder(String passport ,CreateOrderRequestDto request);

    GetOrderResponseDto findOrder(UUID orderId, String passport);

    void updateOrderStatus(UUID orderId,UpdateOrderStatusRequestDto request);

    void updateOrder(UUID orderId, UpdateOrderRequestDto request, String passport);

    void deleteOrder(UUID orderId, String passport);

    PagedModel<OrderSearchResponseDto> search (
            String passport,
            int page,
            int size,
            String sort,
            String orderBy,
            String startDate,
            String endDate
    );
}
