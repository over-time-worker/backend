package com.owlexpress.order.application.service;

import static com.owlexpress.order.common.exception.ExceptionMessage.ORDER_NOT_FOUND_EXCEPTION_MESSAGE;

import com.owlexpress.order.application.dto.request.ConfirmHubStockRequestDto;
import com.owlexpress.order.application.dto.request.CreatePaymentRequestDto;
import com.owlexpress.order.application.dto.response.ConfirmHubStockResponseDto;
import com.owlexpress.order.application.dto.response.CreatePaymentResponseDto;
import com.owlexpress.order.application.dto.response.GetConsumerInfoResponseDto;
import com.owlexpress.order.application.exception.OrderNotFoundException;
import com.owlexpress.order.common.dto.CommonDto;
import com.owlexpress.order.common.dto.PassportDto;
import com.owlexpress.order.common.helper.PassportHelper;
import com.owlexpress.order.common.util.PageUtil;
import com.owlexpress.order.common.constant.OrderStatus;
import com.owlexpress.order.domain.entity.Order;
import com.owlexpress.order.domain.entity.OrderProduct;
import com.owlexpress.order.domain.repository.OrderRepository;
import com.owlexpress.order.infrastructure.client.CartFeignClient;
import com.owlexpress.order.infrastructure.client.ConsumerFeignClient;
import com.owlexpress.order.infrastructure.client.HubFeignClient;
import com.owlexpress.order.infrastructure.client.PaymentFeignClient;
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
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService{
    private final OrderRepository orderRepository;
    private final PassportHelper passportHelper;
    private final ConsumerFeignClient consumerFeignClient;
    private final HubFeignClient hubFeignClient;
    private final PaymentFeignClient paymentFeignClient;
    private final CartFeignClient cartFeignClient;

    @Transactional
    @Override
    public CreateOrderResponseDto createOrder(String passport, CreateOrderRequestDto request) {
        PassportDto passportDto = passportHelper.getPassportDto(passport);

        log.info("consumerId : {}", request.getConsumerId());
        // 1. consumer 업체 정보 조회 feign request
        CommonDto<GetConsumerInfoResponseDto> consumerInfo = consumerFeignClient.getConsumerInfo(
                passport, request.getConsumerId());

        log.info("업체 통과");
        // 2. 허브로 재고 확인 요청
        ConfirmHubStockRequestDto dtos = ConfirmHubStockRequestDto.builder()
                .consumerId(request.getConsumerId())
//                .latitude(consumerInfo.getData().getLongitude())
                .latitude(consumerInfo.getData().getLatitude())
//                .longitude(consumerInfo.getData().getLatitude())
                .longitude(consumerInfo.getData().getLongitude())
                .orderProducts(
                        request.getProducts().stream()
                                .map(hubProduct ->
                                        ConfirmHubStockRequestDto.HubProduct.builder()
                                                .productId(hubProduct.getProductId())
                                                .quantity(hubProduct.getQuantity())
                                                .build()
                                )
                                .collect(Collectors.toList())
                )
                .build();
        CommonDto<ConfirmHubStockResponseDto> hubProductStock = hubFeignClient
                .findHubProductStock(
                    passport, dtos
                );

        log.info("허브 통과");
        // 5. 주문 저장

        BigDecimal calcTotalPrice = calculateTotalPrice(request.getProducts());

        StringBuilder sb = new StringBuilder();
        if (request.getProducts().size() > 1){
            sb.append(request.getProducts().get(0).getProductName())
                    .append("외 ").append(request.getProducts().size() - 1).append("건");
        } else{
            sb.append(request.getProducts().get(0).getProductName());
        }

        Order savedOrder = buildOrder(
                passportDto.getUserId(),
                request,
                calcTotalPrice,
                sb.toString(),
                consumerInfo.getData()
        );

        addOrderProducts(savedOrder, request.getProducts());

        Order save = orderRepository.save(savedOrder);
        save.updateCreateData(passportDto.getUserId());

        // 4. payment service에 모든 정보를 담아 전달
        CreatePaymentRequestDto paymentRequestDto = CreatePaymentRequestDto.builder()
                .orderId(save.getOrderId())
                .price(calculateTotalPrice(request.getProducts()))
                .transactionId(UUID.randomUUID().toString())
                .productInfo(savedOrder.getProductInfo())
                .startHubId(hubProductStock.getData().getHubId())
                .startHubName(hubProductStock.getData().getHubName())
                .orderType(request.getOrderType())
                .shippingAddress(request.getConsumerAddress())
                .description(request.getDescription())
                .requestArrivalTime(request.getRequestArrivalTime())
                .consumerCompanyId(request.getConsumerId())
                .consumerLatitude(consumerInfo.getData().getLatitude())
                .consumerLongitude(consumerInfo.getData().getLongitude())
                .consumerPhoneNumber(consumerInfo.getData().getUserPhoneNumber())
                .consumerName(consumerInfo.getData().getUserName())
                .build();
        CommonDto<CreatePaymentResponseDto> createPaymentResponseDtoCommonDto = paymentFeignClient.sendPaymentRequest(
                passport, paymentRequestDto);

        log.info("결제 통과");
        UUID deliveryId = createPaymentResponseDtoCommonDto.getData().getDeliveryId();
        savedOrder.setDeliveryId(deliveryId);

        // TODO 장바구니 상품 List 삭제 feign
//        cartFeignClient.deleteCartProductsFromOrder();

        return CreateOrderResponseDto.toDto(save);
    }

    @Override
    public GetOrderResponseDto findOrder(UUID orderId, String passport) {
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
    public void updateOrder(UUID orderId, UpdateOrderRequestDto request, String passport) {
        // 현재 구현 : description 수정
        // 이후 고려할 것 : 배송지 수정이 가능하다면,
        // 1. 배송 Service로 FeignClient Request send를 하여 배송 상태 확인
        // 2. 배송중이 아니면 수정 성공
        PassportDto passportDto = passportHelper.getPassportDto(passport);
        Order order = findByOrderId(orderId);
        order.setDescription(request.getDescription(), passportDto.getUserId());
    }

    @Transactional
    @Override
    public void deleteOrder(UUID orderId, String passport) {
        PassportDto passportDto = passportHelper.getPassportDto(passport);
        Order order = findByOrderId(orderId);
        order.deleteOrder(passportDto.getUserId());
    }


    @Override
    public PagedModel<OrderSearchResponseDto> search(
            String passport,
            int page,
            int size,
            String sort,
            String orderBy,
            String startDate,
            String endDate
    ) {
        PassportDto passportDto = passportHelper.getPassportDto(passport);

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

    private Order buildOrder(
            Long userId,
            CreateOrderRequestDto request,
            BigDecimal totalPrice,
            String productInfo,
            GetConsumerInfoResponseDto dto
    ) {
        return Order.builder()
                .userId(userId)
                .consumerId(request.getConsumerId())
                .hubId(dto.getHubId())
                .consumerAddress(request.getConsumerAddress())
                .deliveryId(null)
                .totalPrice(totalPrice)
                .description(request.getDescription())
                .requestArrivalTime(request.getRequestArrivalTime())
                .orderType(request.getOrderType())
                .orderStatus(OrderStatus.COMPLETE)
                .productInfo(productInfo)
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
