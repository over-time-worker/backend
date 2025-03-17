package com.owlexpress.producer.domain.service;

import com.owlexpress.producer.common.helper.ProducerHelper;
import com.owlexpress.producer.domain.entity.Producer;
import com.owlexpress.producer.domain.repository.ProducerRepository;
import com.owlexpress.producer.infrastructure.config.ProductSearchConfig;
import com.owlexpress.producer.presentation.dto.response.ProducerResponseDto;
import com.owlexpress.producer.presentation.dto.response.SearchProducerResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProducerServiceImpl implements ProducerService {
    private final ProducerRepository producerRepository;
    private final ProducerHelper producerHelper;
    private final ProductSearchConfig productSearchConfig;

    @Override
    public ProducerResponseDto find(UUID producerId) {

        Producer producer = producerHelper.getProducer(producerId);

        return ProducerResponseDto.fromEntity(producer);
    }

    @Override
    public PagedModel<SearchProducerResponseDto> search(
            Integer page,
            Integer size,
            String sort,
            String q,
            String orderBy
    ) {
        // 페이지 크기 제한 적용
        if (!productSearchConfig.getAllowedPageSizes()
                                .contains(size)) {
            size = productSearchConfig.getDefaultPageSize();
        }

        // 정렬 기준 제한 적용
        if (!productSearchConfig.getAllowedSorts()
                                .contains(sort)) {
            sort = productSearchConfig.getDefaultSort();
        }

        Sort.Direction direction = orderBy.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        PageRequest pageRequest = PageRequest.of(
                page,
                size,
                Sort.by(
                        direction,
                        sort
                )
        );

        Page<Producer> producers = producerRepository.searchProducer(
                sort,
                q,
                orderBy,
                pageRequest
        );

        Page<SearchProducerResponseDto> responseDtoList = producers.map(SearchProducerResponseDto::fromEntity);

        return new PagedModel<>(responseDtoList);
    }
}
