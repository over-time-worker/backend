package com.owlexpress.user.infrastructure.repository;

import static com.owlexpress.user.domain.entity.QUser.user;

import com.owlexpress.user.common.util.QueryUtil;
import com.owlexpress.user.presentation.dto.response.GetUsersResponseDto;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserQueryDSLImpl implements UserQueryDSLRepository {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<GetUsersResponseDto> searchUsers(Pageable pageable, String keyword) {
        OrderSpecifier<?> orderSpecifier = QueryUtil.getOrderSpecifier(pageable, user);

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        if(keyword != null) {
            booleanBuilder.and(user.username.like(keyword));
        }

        List<GetUsersResponseDto> contents = jpaQueryFactory
                .select(Projections.constructor(GetUsersResponseDto.class,
                                                user.userId,
                                                user.accountId,
                                                user.username,
                                                user.phoneNumber,
                                                user.platformId,
                                                user.platformType,
                                                user.role,
                                                user.approvalStatus,
                                                user.isPublic
                ))
                .from(user)
                .where(
                        user.deletedAt.isNull(),
                        booleanBuilder
                )
                .orderBy(orderSpecifier)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(contents, pageable, contents.size());
    }
}
