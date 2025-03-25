package com.owlexpress.delivery.infrastructure.repository;

import static com.owlexpress.delivery.domain.entity.QDelivery.delivery;
import static com.owlexpress.delivery.domain.entity.QDeliveryHistory.deliveryHistory;

import com.owlexpress.delivery.common.dto.response.DeliveryFindResponseDto;
import com.owlexpress.delivery.common.util.CommonUtil;
import com.owlexpress.delivery.common.util.QueryUtil;
import com.owlexpress.delivery.domain.entity.Delivery;
import com.owlexpress.delivery.domain.entity.constant.DeliveryStatus;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

@Repository
@RequiredArgsConstructor
public class DeliveryQueryRepositoryImpl implements DeliveryQueryRepository {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<Delivery> findDeliveryByIdWithDeliveryHistories(UUID id) {
        return Optional.ofNullable(
                jpaQueryFactory
                        .selectFrom(delivery)
                        .leftJoin(delivery.deliveryHistories, deliveryHistory).fetchJoin()
                        .where(delivery.id.eq(id))
                        .orderBy(deliveryHistory.sequence.asc())
                        .distinct()
                        .fetchOne()
        );
    }

    @Override
    public Page<DeliveryFindResponseDto> search(Pageable pageable, String startDate, String endDate, UUID deliveryId, String deliveryStatus) {
        OrderSpecifier<?> orderSpecifier = QueryUtil.getOrderSpecifier(pageable, delivery);

        List<DeliveryFindResponseDto> contents = jpaQueryFactory
                .select(Projections.constructor(DeliveryFindResponseDto.class,
                        delivery.id,
                        delivery.orderId,
                        delivery.productInfo,
                        delivery.startHubId,
                        delivery.startHubName,
                        delivery.destinationHubId,
                        delivery.destinationHubName,
                        delivery.consumerDeliverId,
                        delivery.orderType,
                        delivery.description,
                        delivery.requestArrivalTime,
                        delivery.totalEstimateDurationTime,
                        delivery.totalEstimateDistance,
                        delivery.deliveryStatus,
                        delivery.consumerCompanyId,
                        delivery.consumerPhoneNumber,
                        delivery.consumerName,
                        delivery.shippingAddress
                ))
                .from(delivery)
                .where(
                        delivery.deletedAt.isNull(),
                        createKeywordForSearch(startDate, endDate, deliveryId, deliveryStatus)
                )
                .orderBy(orderSpecifier)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(contents, pageable, contents.size());
    }

    private BooleanBuilder createKeywordForSearch(String startDate, String endDate, UUID deliveryId, String deliveryStatus) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();

        if (StringUtils.hasText(startDate)) {
            booleanBuilder.and(delivery.createdAt.after(CommonUtil.StringToLocalDateTime(startDate)));
        }

        if (StringUtils.hasText(endDate)) {
            booleanBuilder.and(delivery.createdAt.before(CommonUtil.StringToLocalDateTime(endDate)));
        }

        if (!(deliveryId == null)) {
            booleanBuilder.and(delivery.id.eq(deliveryId));
        }

        if (StringUtils.hasText(deliveryStatus)) {
            booleanBuilder.and(delivery.deliveryStatus.eq(DeliveryStatus.getStatus(deliveryStatus)));
        }

        return booleanBuilder;
    }
}
