package com.owlexpress.hub.domain.service;

import com.owlexpress.hub.domain.entity.Hub;
import com.owlexpress.hub.domain.repository.HubRepository;
import com.owlexpress.hub.presentation.dto.HubCreateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HubServiceImpl implements HubService {

    private final HubRepository hubRepository;

    @Override
    public void create(HubCreateRequestDto requestDto) {
        Hub hub = requestDto.toEntity();
        hubRepository.save(hub);
    }
}
