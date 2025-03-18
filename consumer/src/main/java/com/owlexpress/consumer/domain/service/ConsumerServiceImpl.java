package com.owlexpress.consumer.domain.service;

import com.owlexpress.consumer.common.exceptions.ConsumerException;
import com.owlexpress.consumer.common.util.ConsumerHelper;
import com.owlexpress.consumer.domain.entity.Consumer;
import com.owlexpress.consumer.domain.repository.ConsumerRepository;
import com.owlexpress.consumer.infrastructure.config.ConsumerSearchConfig;
import com.owlexpress.consumer.presentation.dto.response.ConsumerResponseDto;
import com.owlexpress.consumer.presentation.dto.response.SearchConsumerResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ConsumerServiceImpl implements ConsumerService {
    private final ConsumerRepository consumerRepository;
    private final ConsumerSearchConfig consumerSearchConfig;
    private final ConsumerHelper consumerHelper;

    @Override
    @Transactional(readOnly = true)
    public ConsumerResponseDto find(UUID consumerId) {
        Consumer consumer = consumerHelper.getConsumer(consumerId);
        return ConsumerResponseDto.fromEntity(consumer);
    }

    @Override
    public PagedModel<SearchConsumerResponseDto> search(
            Integer page,
            Integer size,
            String sort,
            String q,
            String orderBy
    ) {
        // 페이지 크기 제한 적용
        if (!consumerSearchConfig.getAllowedPageSizes()
                                 .contains(size)) {
            size = consumerSearchConfig.getDefaultPageSize();
        }

        // 정렬 기준 제한 적용
        if (!consumerSearchConfig.getAllowedSorts()
                                 .contains(sort)) {
            sort = consumerSearchConfig.getDefaultSort();
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

        Page<Consumer> consumers = consumerRepository.searchProducer(
                sort,
                q,
                orderBy,
                pageRequest
        );

        Page<SearchConsumerResponseDto> responseDtoList = consumers.map(SearchConsumerResponseDto::fromEntity);

        return new PagedModel<>(responseDtoList);
    }

}
