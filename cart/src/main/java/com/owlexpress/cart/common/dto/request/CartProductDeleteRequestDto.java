package com.owlexpress.cart.common.dto.request;

import java.util.List;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CartProductDeleteRequestDto {
    private List<UUID> productIds;
}
