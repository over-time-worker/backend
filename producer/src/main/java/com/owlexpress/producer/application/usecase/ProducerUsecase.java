package com.owlexpress.producer.application.usecase;

import com.owlexpress.producer.domain.entity.Producer;
import com.owlexpress.producer.domain.repository.ProducerRepository;
import com.owlexpress.producer.infrastructure.feignClient.HubFeignClient;
import com.owlexpress.producer.infrastructure.feignClient.UserFeignClient;
import com.owlexpress.producer.presentation.dto.request.CreateProducerRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProducerUsecase {
    private final UserFeignClient userFeignClient;
    private final ProducerRepository producerRepository;
    private final HubFeignClient hubFeignClient;

    @Transactional
    public void create(CreateProducerRequestDto createProducerRequestDto) {

//        GetUserResponseDto findUserResponseDto = userFeignClient.get(createProducerRequestDto.getUserId());

//        GetHubResponseDto getUserResponseDto = HubhubFeignClient.get(createProducerRequestDto.getHubId());

        //TODO:: client 통신 이후 코드 수정하기, 매개변수에 findUserResponseDto,GetUserResponseDto넣고 통신
        //TODO :: 각 dto는 필요한 데이터를 가져오는 API를 생성해야 하나? 아니면 기존의 API 사용?
        Producer producer = CreateProducerRequestDto.toEntity(createProducerRequestDto);
        producer.updateCreateData(1L);

        producerRepository.save(producer);

    }
}
