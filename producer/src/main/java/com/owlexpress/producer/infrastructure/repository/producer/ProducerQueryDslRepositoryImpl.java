package com.owlexpress.producer.infrastructure.repository.producer;

import com.owlexpress.producer.domain.entity.Producer;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.owlexpress.producer.domain.entity.QProducer.producer;

@Repository
@RequiredArgsConstructor
public class ProducerQueryDslRepositoryImpl implements ProducerQueryDslRepository {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Producer> searchProducer(
            String sort,
            String q,
            String orderBy,
            PageRequest pageRequest
    ) {
        // BooleanBuilder를 사용하여 동적 검색 조건을 생성
        BooleanBuilder builder = new BooleanBuilder();

        // 검색어(q)가 존재하면 producerName에서 해당 검색어를 포함하는 조건 추가
        if (q != null && !q.isEmpty()) {
            builder.and(producer.companyName.containsIgnoreCase(q));
        }

        // 정렬 방향 설정 (desc 또는 asc)
        Order order = orderBy.equalsIgnoreCase("desc") ? Order.DESC : Order.ASC;

        // 정렬 기준에 따라 OrderSpecifier 생성
        OrderSpecifier<?> orderSpecifier = switch (sort) {
            case "name" -> new OrderSpecifier<>(order, producer.companyName);
            case "address" -> new OrderSpecifier<>(order, producer.companyAddress);
            default -> new OrderSpecifier<>(order, producer.producerId); // 기본 정렬 기준: productId
        };

        // QueryDSL을 사용하여 검색 및 페이징 처리
        List<Producer> results = queryFactory
                .selectFrom(producer) // producer 테이블에서 조회
                .where(builder) // 동적 검색 조건 적용
                .orderBy(orderSpecifier) // 정렬 적용
                .offset(pageRequest.getOffset()) // 페이징 offset 설정
                .limit(pageRequest.getPageSize()) // 페이징 size 설정
                .fetch(); // 결과 조회

        // 총 검색 결과 개수 계산
        long total = queryFactory
                .selectFrom(producer)
                .where(builder)
                .fetch().size(); // 전체 개수 조회 , fetchOne Deprecated

        // Page 객체로 변환하여 반환
        return new PageImpl<>(results, pageRequest, total);
    }
}
