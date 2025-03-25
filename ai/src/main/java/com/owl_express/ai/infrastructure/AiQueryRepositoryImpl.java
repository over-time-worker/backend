package com.owl_express.ai.infrastructure;

import static com.owl_express.ai.domain.entity.QAi.ai;

import com.owl_express.ai.common.dto.response.MessageFindResponseDto;
import com.owl_express.ai.common.util.QueryUtil;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AiQueryRepositoryImpl implements AiQueryRepository {
    private final JPAQueryFactory jpaQueryFactory;

    public Page<MessageFindResponseDto> searchMessages(Pageable pageable, UUID keyword) {
        OrderSpecifier<?> orderSpecifier = QueryUtil.getOrderSpecifier(pageable, ai);

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        if(keyword != null) {
            booleanBuilder.and(ai.id.eq(keyword));
        }

        List<MessageFindResponseDto> contents = jpaQueryFactory
                .select(Projections.constructor(MessageFindResponseDto.class,
                        ai.id,
                        ai.response,
                        ai.createdAt,
                        ai.modifiedAt
                ))
                .from(ai)
                .where(
                        ai.deletedAt.isNull(),
                        booleanBuilder
                )
                .orderBy(orderSpecifier)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(contents, pageable, contents.size());
    }
}
