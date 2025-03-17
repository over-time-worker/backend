package com.owlexpress.user.infrastructure.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserQueryDSLImpl implements UserQueryDSLRepository {
    private final JPAQueryFactory jpaQueryFactory;
}
