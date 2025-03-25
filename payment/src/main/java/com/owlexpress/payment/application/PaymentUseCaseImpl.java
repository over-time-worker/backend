package com.owlexpress.payment.application;

import com.owlexpress.payment.common.PassportDto;
import com.owlexpress.payment.application.dto.request.DeliveryCreateRequestDto;
import com.owlexpress.payment.application.dto.request.OptimalRouteRequestDto;
import com.owlexpress.payment.application.dto.response.OrderFindResponseDto;
import com.owlexpress.payment.application.dto.response.PaymentFindResponseDto;
import com.owlexpress.payment.application.dto.response.RouteResponseDto;
import com.owlexpress.payment.common.helper.PassportHelper;
import com.owlexpress.payment.common.exception.PaymentException.OrderDoesNotMatchException;
import com.owlexpress.payment.common.exception.PaymentException.DeliveryCreationFailException;
import com.owlexpress.payment.common.exception.PaymentException.PaymentNotFoundException;
import com.owlexpress.payment.domain.entity.Payment;
import com.owlexpress.payment.domain.repository.PaymentRepository;
import com.owlexpress.payment.infrastructure.client.DeliveryClient;
import com.owlexpress.payment.infrastructure.client.HubIntervalInfoClient;
import com.owlexpress.payment.infrastructure.client.OrderClient;
import com.owlexpress.payment.presentation.dto.CommonDto;
import com.owlexpress.payment.presentation.dto.request.PaymentCreateRequestDto;
import com.owlexpress.payment.presentation.dto.request.PaymentDeleteRequestDto;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentUseCaseImpl implements com.owlexpress.payment.presentation.PaymentUseCase {

    private final PaymentRepository paymentRepository;
    private final OrderClient orderClient;
    private final HubIntervalInfoClient hubIntervalInfoClient;
    private final DeliveryClient deliveryClient;
    private final PassportHelper passportHelper;

    @Transactional
    @Override
    public UUID createPayment(PaymentCreateRequestDto requestDto, String passport) {
        Payment payment = requestDto.toEntity();
        // TODO : 결제사 승인 요청

        PassportDto passportDto = passportHelper.getPassportDto(passport);
        // TODO : HubClient로 재고가 있는 허브 ID 전달.

        payment.createdEntity(passportDto.getUserId());
        paymentRepository.save(payment);

        LocalDateTime departureTime = payment.getCreatedAt();
        OptimalRouteRequestDto hubRouteRequestDto = OptimalRouteRequestDto.builder()
                .startHubId(requestDto.getStartHubId())
                .consumerId(requestDto.getConsumerCompanyId())
                .consumerLatitude(requestDto.getConsumerLatitude())
                .consumerLongitude(requestDto.getConsumerLongitude())
                .departureTime(departureTime)
                .build();

        RouteResponseDto route = hubIntervalInfoClient.findOptimalPath(hubRouteRequestDto);

        DeliveryCreateRequestDto deliveryCreateRequestDto = DeliveryCreateRequestDto.builder()
                .orderId(requestDto.getOrderId())
                .productInfo(requestDto.getProductInfo())
                .startHubId(requestDto.getStartHubId())
                .startHubName(requestDto.getStartHubName())
                .destinationHubId(route.getDestinationHubId())
                .destinationHubName(route.getDestinationHubName())
                .orderType(requestDto.getOrderType())
                .shippingAddress(requestDto.getShippingAddress())
                .description(requestDto.getDescription())
                .requestArrivalTime(requestDto.getRequestArrivalTime())
                .consumerCompanyId(requestDto.getConsumerCompanyId())
                .consumerPhoneNumber(requestDto.getConsumerPhoneNumber())
                .consumerName(requestDto.getConsumerName())
                .totalEstimateDistance(route.getTotalEstimateDistance())
                .totalEstimateDurationTime(route.getTotalEstimateDurationTime())
                .hubList(route.getHubList())
                .build();

        CommonDto<Map<String, UUID>> delivery = deliveryClient.createDelivery(
                deliveryCreateRequestDto,
                passport);

        if (delivery.getStatus() != HttpStatus.CREATED) {
            throw new DeliveryCreationFailException();
        }

        return delivery.getData().getOrDefault("deliveryId", null);

    }

    @Transactional
    @Override
    public void deletePayment(PaymentDeleteRequestDto requestDto, String passport) {
        Payment payment = paymentRepository.findByTransactionId(requestDto.getTransactionId())
                .orElseThrow(PaymentNotFoundException::new);

        // TODO : 결제사 취소 요청

        PassportDto passportDto = passportHelper.getPassportDto(passport);

        payment.deleteEntity(passportDto.getUserId());
    }

    @Override
    public PaymentFindResponseDto find(UUID orderId) {
        Payment payment = paymentRepository.findByOrderId(orderId)
                .orElseThrow(OrderDoesNotMatchException::new);

        OrderFindResponseDto data = orderClient.findOrderDetails(orderId).getData();
        PaymentFindResponseDto paymentFindResponseDto = data.toPaymentFindResponseDto();
        paymentFindResponseDto.setInfo(payment);

        return paymentFindResponseDto;

    }
}
