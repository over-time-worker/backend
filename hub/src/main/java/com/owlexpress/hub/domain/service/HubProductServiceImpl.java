package com.owlexpress.hub.domain.service;

import com.owlexpress.hub.application.dto.response.HubProductIsEnoughResponseDto;
import com.owlexpress.hub.common.dto.response.PassportDto;
import com.owlexpress.hub.common.exception.HubException;
import com.owlexpress.hub.common.exception.HubProductException;
import com.owlexpress.hub.common.helper.PassportHelper;
import com.owlexpress.hub.domain.entity.HubProduct;
import com.owlexpress.hub.domain.repository.HubIntervalInfoRepository;
import com.owlexpress.hub.domain.repository.HubProductRepository;
import com.owlexpress.hub.domain.repository.HubRepository;
import com.owlexpress.hub.presentation.dto.request.HubProductCheckRequestDto;
import com.owlexpress.hub.presentation.dto.request.HubProductUpdateRequestDto;
import com.owlexpress.hub.presentation.dto.response.HubProductFindResponseDto;
import com.owlexpress.hub.presentation.dto.response.HubProductSearchResponseDto;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class HubProductServiceImpl implements HubProductService {

    private final HubProductRepository hubProductRepository;
    private final PassportHelper passportHelper;
    private final HubRepository hubRepository;
    private static final List<Integer> ALLOWED_SIZES = List.of(10, 30, 50);
    private static final Integer DEFAULT_SIZE = 10;
    private final HubIntervalInfoRepository hubIntervalInfoRepository;


    @Transactional(readOnly = true)
    public HubProductFindResponseDto findHubProductByProductId(UUID productId) {
        HubProduct hubProduct = hubProductRepository.findByProductId(productId)
                .orElseThrow(HubException.HubProductNotFoundException::new);

        return HubProductFindResponseDto.fromEntity(hubProduct);

    }


    @Override
    @Transactional
    public void update(HubProductUpdateRequestDto requestDto, String passport) {
        PassportDto passportDto = passportHelper.getPassportDto(passport);
        HubProduct hubProduct = hubRepository.findByHubProductId(requestDto.getHubProductId())
                .orElseThrow(HubProductException.HubProductNotFoundException::new);
        hubProduct.updateEntity(requestDto);

        hubProduct.modifiedEntity(passportDto.getUserId());
    }

    /*
    허브 상품
     */
    @Override
    @Transactional(readOnly = true)
    public PagedModel<HubProductSearchResponseDto> searchHubProduct(
            int page,
            int size,
            String sort,
            String q,
            String orderBy
    ) {
        Sort.Direction direction =
                sort.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        if (!ALLOWED_SIZES.contains(size)) {
            size = DEFAULT_SIZE;
        }
        Sort sortAndOrderBy = Sort.by(direction, orderBy); // direction 활용 추가
        PageRequest pageRequest = PageRequest.of(page, size, sortAndOrderBy);
        return hubRepository.searchHubProduct(pageRequest, q, orderBy, sort);
    }


    @Override
    @Transactional(readOnly = true)
    public HubProductFindResponseDto findHubProduct(UUID hubProductId) {
        HubProduct hubProduct = hubProductRepository.findByHubProductId(hubProductId)
                .orElseThrow(HubProductException.HubProductNotFoundException::new);

        return HubProductFindResponseDto.fromEntity(hubProduct);
    }

    @Override
    @Transactional(readOnly = true)
    public List<HubProductIsEnoughResponseDto> checkHubProductStocks(
            List<HubProductCheckRequestDto> requestDto
    ) {
        return requestDto.stream()
                .map(dto -> HubProductIsEnoughResponseDto.builder()
//                        .hubProductId(dto.getHubProductId())
                        .hubProductId(dto.getHubProductId())
                        .isEnough(true)
                        .build()
                ).toList();
    }
}

