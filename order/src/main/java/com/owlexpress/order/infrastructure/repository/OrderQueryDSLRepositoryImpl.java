package com.owlexpress.order.infrastructure.repository;

import static com.owlexpress.order.domain.entity.QOrder.order;
import static com.owlexpress.order.domain.entity.QOrderProduct.orderProduct;

import com.owlexpress.order.common.util.CommonUtil;
import com.owlexpress.order.common.util.QueryUtil;
import com.owlexpress.order.domain.entity.Order;
import com.owlexpress.order.presentation.dto.response.GetOrderProductResponseDto;
import com.owlexpress.order.presentation.dto.response.OrderSearchResponseDto;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
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

        // 1. 주문 엔티티 페이징 조회
        List<Order> orders = jpaQueryFactory
                .selectFrom(order)
                .where(
                        order.deletedAt.isNull(),
                        createDateCondition(startDate, endDate)
                )
                .orderBy(QueryUtil.getOrderSpecifier(pageable, order))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 2. 주문 ID 목록 추출
        List<UUID> orderIds = orders.stream()
                .map(Order::getOrderId)
                .collect(Collectors.toList());

        // 3. 주문 상품 조회 (In 절 사용)
        Map<UUID, List<GetOrderProductResponseDto>> orderProductMap = jpaQueryFactory
                .select(
                        Projections.constructor(
                            GetOrderProductResponseDto.class,
                            orderProduct.orderProductId,
                            orderProduct.orderId,
                            orderProduct.productId,
                            orderProduct.quantity,
                            orderProduct.productName,
                            orderProduct.productType,
                            orderProduct.amount,
                            orderProduct.price
                        )
                )
                .from(orderProduct)
                .where(orderProduct.orderId.in(orderIds))
                .fetch()
                .stream()
                .collect(Collectors.groupingBy(GetOrderProductResponseDto::getOrderId));

        // 4. DTO 변환
        List<OrderSearchResponseDto> content = orders.stream()
                .map(order -> convertToDto(order, orderProductMap))
                .collect(Collectors.toList());

        // 5. 전체 개수 조회
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

    private OrderSearchResponseDto convertToDto(
            Order order,
            Map<UUID, List<GetOrderProductResponseDto>> orderProductMap
    ) {
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
                        orderProductMap.getOrDefault(order.getOrderId(), Collections.emptyList())
                )
                .createdAt(order.getCreatedAt())
                .modifiedAt(order.getModifiedAt())
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
