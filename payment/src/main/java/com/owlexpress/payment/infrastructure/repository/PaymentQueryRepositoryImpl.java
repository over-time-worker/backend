package com.owlexpress.payment.infrastructure.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PaymentQueryRepositoryImpl implements PaymentQueryRepository {

    private final JPAQueryFactory jpaQueryFactory;
}
