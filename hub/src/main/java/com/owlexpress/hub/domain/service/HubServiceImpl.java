package com.owlexpress.hub.domain.service;

import com.owlexpress.hub.common.exception.HubException.HubNotFoundException;
import com.owlexpress.hub.domain.entity.Hub;
import com.owlexpress.hub.domain.repository.HubRepository;
import com.owlexpress.hub.presentation.dto.request.HubCreateRequestDto;
import com.owlexpress.hub.presentation.dto.request.HubUpdateRequestDto;
import lombok.RequiredArgsConstructor;
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
}
