package com.owl_express.ai.infrastructure;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AiQueryRepositoryImpl implements AiQueryRepository {
    private final JPAQueryFactory jpaQueryFactory;
}
