package com.owlexpress.order.application.service;

import static com.owlexpress.order.common.exception.ExceptionMessage.ORDER_NOT_FOUND_EXCEPTION_MESSAGE;

import com.owlexpress.order.application.exception.OrderNotFoundException;
import com.owlexpress.order.common.util.PageUtil;
import com.owlexpress.order.domain.entity.Order;
import com.owlexpress.order.domain.entity.OrderProduct;
import com.owlexpress.order.domain.repository.OrderRepository;
import com.owlexpress.order.presentation.dto.request.CreateOrderProductRequestDto;
import com.owlexpress.order.presentation.dto.request.CreateOrderRequestDto;
import com.owlexpress.order.presentation.dto.request.UpdateOrderRequestDto;
import com.owlexpress.order.presentation.dto.request.UpdateOrderStatusRequestDto;
import com.owlexpress.order.presentation.dto.response.CreateOrderResponseDto;
import com.owlexpress.order.presentation.dto.response.GetOrderResponseDto;
import com.owlexpress.order.presentation.dto.response.OrderSearchResponseDto;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService{
    private final OrderRepository orderRepository;

    @Transactional
    @Override
    public CreateOrderResponseDto createOrder(Long userId, CreateOrderRequestDto request) {

        BigDecimal calcTotalPrice = calculateTotalPrice(request.getProducts());

        Order order = buildOrder(userId, request, calcTotalPrice);

        addOrderProducts(order, request.getProducts());

        Order save = orderRepository.save(order);
        save.updateCreateData(userId);

        // TODO:: 1. Payment Service로 FeignClient Request send
        // TODO:: 2. Transaction이 정상적으로 종료되면 Cart Service로 FeignClient Request send하여 항목 삭제

        return CreateOrderResponseDto.toDto(save);
    }

    @Override
    public GetOrderResponseDto findOrder(UUID orderId) {
        Order order = findByOrderId(orderId);
        return GetOrderResponseDto.toDto(order);
    }

    // TODO : 현재는 주문의 상태를 변경하는 상황이 일어나지 않음.
    @Override
    public void updateOrderStatus(UUID orderId, UpdateOrderStatusRequestDto request) {
        // 주문이 취소(CANCEL)가 아님 and 주문 상태가 대기(PENDING) 이여야 함.
//        Order order = findByOrderId(orderId);
//
//        if (order.getOrderStatus().equals(OrderStatus.PENDING)) {
//
//        } else {
//            throw new RuntimeException();
//        }
    }

    @Transactional
    @Override
    public void updateOrder(UUID orderId, UpdateOrderRequestDto request, Long userId) {
        // 현재 구현 : description 수정
        // 이후 고려할 것 : 배송지 수정이 가능하다면,
        // 1. 배송 Service로 FeignClient Request send를 하여 배송 상태 확인
        // 2. 배송중이 아니면 수정 성공
        Order order = findByOrderId(orderId);
        order.setDescription(request.getDescription(), userId);
    }

    @Transactional
    @Override
    public void deleteOrder(UUID orderId, Long userId) {
        Order order = findByOrderId(orderId);
        order.deleteOrder(userId);
    }


    @Override
    public PagedModel<OrderSearchResponseDto> search(
            int page,
            int size,
            String sort,
            String orderBy,
            String startDate,
            String endDate
    ) {
        Pageable pageable = PageUtil.getPageable(page, size, sort, orderBy);
        Page<OrderSearchResponseDto> paged = orderRepository.search(
                pageable,
                startDate,
                endDate
        );
        return new PagedModel<>(paged);
    }

    private Order findByOrderId(UUID orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(()-> new OrderNotFoundException(ORDER_NOT_FOUND_EXCEPTION_MESSAGE));
    }

    private BigDecimal calculateTotalPrice(List<CreateOrderProductRequestDto> orderProducts) {
        return orderProducts.stream()
                .map(product -> product.getPrice()
                        .multiply(BigDecimal.valueOf(product.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private Order buildOrder(Long userId, CreateOrderRequestDto request, BigDecimal totalPrice) {
        return Order.builder()
                .userId(userId)
                .consumerId(request.getConsumerId())
                .hubId(request.getHubId())
                .consumerAddress(request.getConsumerAddress())
                .deliveryId(request.getDeliveryId())
                .totalPrice(totalPrice)
                .description(request.getDescription())
                .requestArrivalTime(request.getRequestArrivalTime())
                .orderType(request.getOrderType())
                .orderStatus(request.getOrderStatus())
                .productInfo(request.getProductInfo())
                .build();
    }

    private void addOrderProducts(Order order, List<CreateOrderProductRequestDto> orderProducts) {
        orderProducts.forEach(product -> {
            BigDecimal calculatedAmount = product
                    .getPrice()
                    .multiply(BigDecimal.valueOf(product.getQuantity()));

            OrderProduct orderProduct = OrderProduct.builder()
                    .orderId(order.getOrderId())
                    .productId(product.getProductId())
                    .quantity(product.getQuantity())
                    .productName(product.getProductName())
                    .productType(product.getProductType())
                    .amount(calculatedAmount)
                    .price(product.getPrice())
                    .build();

            order.addOrderProduct(orderProduct);
        });
    }
}
