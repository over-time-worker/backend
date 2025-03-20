package com.owl_express.alarm.infrastructure.repository;

import static com.owl_express.alarm.domain.entity.QAlarm.alarm;

import com.owl_express.alarm.application.dtos.response.AlarmFindResponseDto;
import com.owl_express.alarm.application.dtos.response.AlarmSearchResponseDto;
import com.owl_express.alarm.common.util.CommonUtil;
import com.owl_express.alarm.common.util.QueryUtil;
import com.owl_express.alarm.domain.entity.Alarm.PlatformType;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

@Repository
@RequiredArgsConstructor
public class AlarmQueryRepositoryImpl implements AlarmQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<AlarmSearchResponseDto> search(Pageable pageable, String startDate, String endDate, String deliveryUserId, String platformType) {
        OrderSpecifier<?> orderSpecifier = QueryUtil.getOrderSpecifier(pageable, alarm);

        List<AlarmSearchResponseDto> contents = jpaQueryFactory
                .select(Projections.constructor(AlarmSearchResponseDto.class,
                        alarm.id,
                        alarm.userId,
                        alarm.platformType,
                        alarm.userChannelId,
                        alarm.message,
                        alarm.messageType,
                        alarm.createdAt,
                        alarm.modifiedAt
                ))
                .from(alarm)
                .where(
                        alarm.deletedAt.isNull(),
                        createKeywordForSearch(startDate, endDate, deliveryUserId, platformType)
                )
                .orderBy(orderSpecifier)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(contents, pageable, contents.size());
    }

    private BooleanBuilder createKeywordForSearch(String startDate, String endDate, String deliveryUserId, String platformType) {
        BooleanBuilder booleanBuilder = new BooleanBuilder();

        if (StringUtils.hasText(startDate)) {
            booleanBuilder.and(alarm.createdAt.after(CommonUtil.StringToLocalDateTime(startDate)));
        }

        if (StringUtils.hasText(endDate)) {
            booleanBuilder.and(alarm.createdAt.before(CommonUtil.StringToLocalDateTime(endDate)));
        }

        if (StringUtils.hasText(deliveryUserId)) {
            booleanBuilder.and(alarm.userId.eq(Long.parseLong(deliveryUserId)));
        }

        if (StringUtils.hasText(platformType)) {
            booleanBuilder.and(alarm.platformType.eq(PlatformType.getType(platformType)));
        }

        return booleanBuilder;
    }


}
