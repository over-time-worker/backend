package com.owlexpress.order.presentation.dto.request;

import com.owlexpress.order.domain.constant.OrderStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UpdateOrderStatusRequestDto {
    private OrderStatus orderStatus;
}
