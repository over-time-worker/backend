package com.owlexpress.hub.domain.service;

import com.owlexpress.hub.common.HubHelper;
import com.owlexpress.hub.common.exception.HubException.HubNotFoundException;
import com.owlexpress.hub.domain.entity.Hub;
import com.owlexpress.hub.domain.repository.HubRepository;
import com.owlexpress.hub.presentation.dto.request.HubCreateRequestDto;
import com.owlexpress.hub.presentation.dto.response.HubFindResponseDto;
import com.owlexpress.hub.presentation.dto.request.HubUpdateRequestDto;
import com.owlexpress.hub.presentation.dto.response.HubSearchResponseDto;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HubServiceImpl implements HubService {

    private final HubRepository hubRepository;

    @Override
    @Transactional
    public void create(HubCreateRequestDto requestDto) {
        Hub hub = requestDto.toEntity();
        // TODO: 패스포트 토큰에서 값 뺴서 집어넣기

        hub.createdEntity(1L);
        hubRepository.save(hub);
    }

    @Override
    @Transactional
    public void update(HubUpdateRequestDto requestDto) {
        Hub origin = hubRepository.findByHubId(requestDto.getHubId())
                .orElseThrow(HubNotFoundException::new);

        // TODO: 패스포트 토큰에서 값 뺴서 집어넣기
        origin.modifiedEntity(1L);
        origin.update(requestDto);
    }

    @Override
    public PagedModel<HubSearchResponseDto> search(
            int page,
            int size,
            String sort,
            String q,
            String orderBy
    ) {
        Sort.Direction direction = sort.equalsIgnoreCase("asc") ? Direction.ASC : Direction.DESC;
        if (!List.of(10, 30, 50).contains(size)) {
            size = 10;
        }

        PageRequest pageRequest = PageRequest.of(page, size);

        return hubRepository.searchHub(pageRequest, q, sort, orderBy);
    }

    @Override
    public HubFindResponseDto find(UUID hubID) {
        return HubFindResponseDto.fromEntity(HubHelper.findByHubId(hubID, hubRepository));

    }
}
