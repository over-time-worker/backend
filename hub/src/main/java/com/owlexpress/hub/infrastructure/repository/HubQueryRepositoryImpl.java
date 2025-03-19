package com.owlexpress.hub.infrastructure.repository;

import com.owlexpress.hub.domain.entity.Hub;
import com.owlexpress.hub.domain.entity.HubProduct;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.owlexpress.hub.domain.entity.QHub.hub;
import static com.owlexpress.hub.domain.entity.QHubProduct.hubProduct;

@Repository
@RequiredArgsConstructor
public class HubQueryRepositoryImpl implements HubQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Hub> searchHub(Pageable pageable, String keyword, String sort, String orderBy) {
        // BooleanBuilder를 사용하여 동적 검색 조건을 생성
        BooleanBuilder builder = new BooleanBuilder();

        // 검색어(keyword)가 존재하면 hubAddress에서 해당 검색어를 포함하는 조건 추가
        if (keyword != null && !keyword.isEmpty()) {
            builder.and(hub.hubAddress.containsIgnoreCase(keyword));
        }

        // 정렬 방향 설정 (desc 또는 asc)
        Order order = orderBy.equalsIgnoreCase("desc") ? Order.DESC : Order.ASC;

        // 정렬 기준에 따라 OrderSpecifier 생성
        OrderSpecifier<?> orderSpecifier = switch (sort) {
            case "name" -> new OrderSpecifier<>(order, hub.name);
            case "address" -> new OrderSpecifier<>(order, hub.hubAddress);
            default -> new OrderSpecifier<>(order, hub.hubId); // 기본 정렬 기준: hubId
        };

        // QueryDSL을 사용하여 검색 및 페이징 처리
        List<Hub> results = queryFactory
                .selectFrom(hub)
                .where(builder) // 검색 조건 추가
                .orderBy(orderSpecifier) // 정렬 적용
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 총 검색 결과 개수 계산
        long total = queryFactory
                .selectFrom(hub)
                .where(builder)
                .fetch().size(); // 전체 개수 조회 , fetchOne Deprecated

        // Page 객체로 변환하여 반환
        return new PageImpl<>(results, pageable, total);
    }

    @Override
    public Page<HubProduct> searchHubProduct(Pageable pageable, String keyword, String orderBy,
            String sort) {
        // BooleanBuilder를 사용하여 동적 검색 조건을 생성
        BooleanBuilder builder = new BooleanBuilder();

        // 검색어(keyword)가 존재하면
        if (keyword != null && !keyword.isEmpty()) {
            builder.and(hubProduct.productName.containsIgnoreCase(keyword));
        }
        // 정렬 방향 설정 (desc 또는 asc)
        Order order = orderBy.equalsIgnoreCase("desc") ? Order.DESC : Order.ASC;

        // 정렬 기준에 따라 OrderSpecifier 생성
        OrderSpecifier<?> orderSpecifier = switch (sort) {
            case "name" -> new OrderSpecifier<>(order, hubProduct.productName);
            case "address" -> new OrderSpecifier<>(order, hub.hubAddress);
            default -> new OrderSpecifier<>(order, hubProduct.hubProductId); // 기본 정렬 기준: 상품 등록 순
        };

        List<HubProduct> hubProducts = queryFactory
                .selectDistinct(hubProduct)
                .from(hub)
                .join(hub.hubProduct, hubProduct)
                .where(builder) // 특정 이름의 제품이 있는 허브만 조회
                .fetch();

        long count = queryFactory.select(hubProduct.count())
                .from(hubProduct)
                .where(builder)
                .fetchOne().longValue();

        return new PageImpl<>(hubProducts, pageable, count);
    }
}
