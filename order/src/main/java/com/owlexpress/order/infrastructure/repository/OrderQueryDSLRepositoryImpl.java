package com.owlexpress.order.infrastructure.repository;

import static com.owlexpress.order.domain.entity.QOrder.order;
import static com.owlexpress.order.domain.entity.QOrderProduct.orderProduct;

import com.owlexpress.order.common.util.CommonUtil;
import com.owlexpress.order.common.util.QueryUtil;
import com.owlexpress.order.domain.entity.Order;
import com.owlexpress.order.domain.entity.OrderProduct;
import com.owlexpress.order.presentation.dto.response.GetOrderProductResponseDto;
import com.owlexpress.order.presentation.dto.response.OrderSearchResponseDto;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

@Repository
@RequiredArgsConstructor
public class OrderQueryDSLRepositoryImpl implements OrderQueryDSLRepository {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<OrderSearchResponseDto> search(Pageable pageable, String startDate, String endDate) {
        // 1. 주문 + 주문 상품 조인 조회
        List<Order> orders = jpaQueryFactory
                .selectFrom(order)
                .leftJoin(order.orderProducts, orderProduct).fetchJoin()
                .where(
                        order.deletedAt.isNull(),
                        createDateCondition(startDate, endDate)
                )
                .orderBy(QueryUtil.getOrderSpecifier(pageable, order))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 2. DTO 변환
        List<OrderSearchResponseDto> content = orders.stream()
                .map(order -> convertToDto(order, order.getOrderProducts()))
                .collect(Collectors.toList());

        // 3. 전체 개수 조회
        long total = jpaQueryFactory
                .select(order.count())
                .from(order)
                .where(
                        order.deletedAt.isNull(),
                        createDateCondition(startDate, endDate)
                )
                .fetchFirst();

        return new PageImpl<>(content, pageable, total);
    }

    private OrderSearchResponseDto convertToDto(Order order, List<OrderProduct> orderProducts) {
        return OrderSearchResponseDto.builder()
                .orderId(order.getOrderId())
                .userId(order.getUserId())
                .consumerId(order.getConsumerId())
                .hubId(order.getHubId())
                .consumerAddress(order.getConsumerAddress())
                .deliveryId(order.getDeliveryId())
                .totalPrice(order.getTotalPrice())
                .description(order.getDescription())
                .requestArrivalTime(order.getRequestArrivalTime())
                .orderType(order.getOrderType())
                .orderStatus(order.getOrderStatus())
                .productInfo(order.getProductInfo())
                .orderProducts(
                        orderProducts.stream()
                                .map(this::convertToProductDto) // OrderProduct → GetOrderProductResponseDto 변환
                                .collect(Collectors.toList())
                )
                .createdAt(order.getCreatedAt())
                .modifiedAt(order.getModifiedAt())
                .build();
    }

    private GetOrderProductResponseDto convertToProductDto(OrderProduct orderProduct) {
        return GetOrderProductResponseDto.builder()
                .orderProductId(orderProduct.getOrderProductId())
                .orderId(orderProduct.getOrderId())
                .productId(orderProduct.getProductId())
                .quantity(orderProduct.getQuantity())
                .productName(orderProduct.getProductName())
                .productType(orderProduct.getProductType())
                .amount(orderProduct.getAmount())
                .price(orderProduct.getPrice())
                .build();
    }

    private BooleanBuilder createDateCondition(String startDate, String endDate) {
        BooleanBuilder builder = new BooleanBuilder();

        if (StringUtils.hasText(startDate)) {
            builder.and(order.createdAt.goe(CommonUtil.StringToLocalDateTime(startDate)));
        }
        if (StringUtils.hasText(endDate)) {
            builder.and(order.createdAt.loe(CommonUtil.StringToLocalDateTime(endDate)));
        }
        return builder;
    }
}
