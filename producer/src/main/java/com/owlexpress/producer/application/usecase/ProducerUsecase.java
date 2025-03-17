package com.owlexpress.producer.application.usecase;

import com.owlexpress.producer.common.util.GeoUtil;
import com.owlexpress.producer.domain.entity.Producer;
import com.owlexpress.producer.domain.repository.ProducerRepository;
import com.owlexpress.producer.infrastructure.feignClient.HubFeignClient;
import com.owlexpress.producer.infrastructure.feignClient.UserFeignClient;
import com.owlexpress.producer.common.dto.request.CreateProducerRequestDto;
import com.owlexpress.producer.common.dto.request.UpdateProductRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

import static com.owlexpress.producer.common.exception.ProducerException.*;

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

        producerRepository.findByCompanyName(createProducerRequestDto.getCompanyName())
                          .ifPresent(producer -> {
                              throw new ProducerNameDuplicateExceptoin("이미 존재하는 업체명입니다.");
                          });
        Producer producer = CreateProducerRequestDto.toEntity(createProducerRequestDto);
        producer.updateCreateData(1L);

        producerRepository.save(producer);

    }

    @Transactional
    public void update(
            UpdateProductRequestDto updateProductRequestDto,
            UUID producerId
    ) {
        //TODO:: 본인의 생성업체가 맞는지 확인 필요
        // passport로 가져온 ID와 대조하기
        Producer producer = getProducer(producerId);

        //TODO:: 가져온 데이터의 정보를 통해 기존 producer의 데이터 수정
        //        GetUserResponseDto findUserResponseDto = userFeignClient.get(createProducerRequestDto.getUserId());
        //        GetHubResponseDto getUserResponseDto = HubhubFeignClient.get(createProducerRequestDto.getHubId());
        updateProducer(producer, updateProductRequestDto);
        producer.updateModifiedData(1L);

        //TODO :: 생산업체 정보가 수정되었음을 연결된 허브 및 상품에 전파해야함
        // 많은 곳에 FeignClient 통신을 보내야하기 때문에 메세지 큐의 필요성을 느끼게됨... 이벤트리스너야 보고싶다
    }

    private Producer getProducer(UUID producerId) {
       return producerRepository.findById(producerId).orElseThrow(
                ()-> new ProducerNotFoundException("찾는 영업이 존재하지 않습니다.")
        );
    }

    public void updateProducer(Producer producer, UpdateProductRequestDto dto) {
        //TODO:: 데이터를 어떤것만 가져올지 정책적으로 정한 후에 코드 수정하기
        Optional.ofNullable(dto.getUserId()).ifPresent(producer::setUserId);
        Optional.ofNullable(dto.getBusinessNumber()).ifPresent(producer::setBusinessNumber);
        Optional.ofNullable(dto.getCompanyName()).ifPresent(producer::setCompanyName);
        Optional.ofNullable(dto.getCompanyType()).ifPresent(producer::setCompanyType);
        Optional.ofNullable(dto.getCompanyAddress()).ifPresent(producer::setCompanyAddress);
        if(Optional.ofNullable(dto.getLatitude()).isPresent() && Optional.ofNullable(dto.getLongitude()).isPresent()){
            Point point = GeoUtil.createPoint(
                    dto.getLatitude(),
                    dto.getLongitude()
            );
            producer.setLocation(point);
        }
        Optional.ofNullable(dto.getHubId()).ifPresent(producer::setHubId);
    }
}
