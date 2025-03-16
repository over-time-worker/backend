package com.owl_express.ai.common.util;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.dsl.Expressions;
import org.springframework.data.domain.Sort;
import com.querydsl.core.types.OrderSpecifier;
import org.springframework.data.domain.Pageable;
import com.querydsl.core.types.Path;

import org.springframework.data.domain.Sort.Direction;
import org.springframework.util.ObjectUtils;

public class QueryUtil {
    public static OrderSpecifier<?> getOrderSpecifier(Pageable pageable, Path<?> path) {
        Sort.Order order = pageable.getSort()
                    .stream()
                    .findFirst()
                    .orElse(Sort.Order.by("createAt").with(Direction.DESC));

        Order direction = order.getDirection().isAscending() ? Order.ASC : Order.DESC;
        Path<?> fieldPath = Expressions.path(path.getType(), path, order.getProperty());

        return new OrderSpecifier(direction, fieldPath);
    }
}
