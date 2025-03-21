package com.owlexpress.hub.domain.service;

import com.owlexpress.hub.application.dto.response.HubProductIsEnoughResponseDto;
import com.owlexpress.hub.presentation.dto.request.HubProductCheckRequestDto;
import com.owlexpress.hub.presentation.dto.request.HubProductUpdateRequestDto;
import com.owlexpress.hub.presentation.dto.response.HubProductFindResponseDto;
import com.owlexpress.hub.presentation.dto.response.HubProductSearchResponseDto;
import org.springframework.data.web.PagedModel;

import java.util.List;
import java.util.UUID;

public interface HubProductService {
    HubProductFindResponseDto findHubProductByProductId(UUID productId);

    void update(HubProductUpdateRequestDto requestDto, String passport);

    /*
 허브 상품
 */
    PagedModel<HubProductSearchResponseDto> searchHubProduct(int page, int size, String sort,
                                                             String q, String orderBy);


    HubProductFindResponseDto findHubProduct(UUID hubProductId);


    List<HubProductIsEnoughResponseDto> checkHubProductStocks(
            List<HubProductCheckRequestDto> requestDto
    );

}
