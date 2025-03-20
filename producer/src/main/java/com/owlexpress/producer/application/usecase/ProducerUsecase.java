package com.owlexpress.producer.application.usecase;

import com.owlexpress.producer.application.dto.request.UpdateProductDto;
import com.owlexpress.producer.common.dto.request.CreateProducerRequestDto;
import com.owlexpress.producer.common.dto.request.UpdateProducerRequestDto;
import com.owlexpress.producer.common.dto.response.GetUserInfoResponseDto;
import com.owlexpress.producer.common.exception.ProducerException;
import com.owlexpress.producer.common.helper.ProducerHelper;
import com.owlexpress.producer.common.util.GeoUtil;
import com.owlexpress.producer.domain.entity.Producer;
import com.owlexpress.producer.domain.repository.ProducerRepository;
import com.owlexpress.producer.infrastructure.feignClient.HubProductClient;
import com.owlexpress.producer.infrastructure.feignClient.ProductClient;
import com.owlexpress.producer.infrastructure.feignClient.UserFeignClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

import static com.owlexpress.producer.common.exception.ExceptionMessage.PRODUCER_DUPLICATE_NAME_MESSAGE;
import static com.owlexpress.producer.common.exception.ExceptionMessage.PRODUCER_NOT_AUTHORIZED_UPDATE_MESSAGE;
import static com.owlexpress.producer.common.exception.ProducerException.ProducerNameDuplicateException;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProducerUsecase {
    private final UserFeignClient userFeignClient;
    private final ProducerRepository producerRepository;
    private final ProducerHelper producerHelper;
    private final ProductClient productClient;

    @Transactional
    public void create(CreateProducerRequestDto createProducerRequestDto) {

        GetUserInfoResponseDto getUserInfoResponseDto = userFeignClient.get(createProducerRequestDto.getUserId())
                                                                       .getData();

        producerRepository.findByCompanyName(createProducerRequestDto.getCompanyName())
                          .ifPresent(producer -> {
                              throw new ProducerNameDuplicateException(PRODUCER_DUPLICATE_NAME_MESSAGE);
                          });

        Producer producer = CreateProducerRequestDto.toEntity(createProducerRequestDto, getUserInfoResponseDto);
        producer.updateCreateData(1L);

        producerRepository.save(producer);

    }

    @Transactional
    public void update(
            UpdateProducerRequestDto updateProducerRequestDto,
            UUID producerId
    ) {
        //TODO:: 본인의 생성업체가 맞는지 확인 필요
        // passport로 가져온 ID와 대조하기
        Producer producer = producerHelper.getProducer(producerId);
        if (!producer.getUserId()
                     .equals(1L)) {
            throw new ProducerException.NotAuthorizedException(PRODUCER_NOT_AUTHORIZED_UPDATE_MESSAGE);
        }

        //TODO:: 가져온 데이터의 정보를 통해 기존 producer의 데이터 수정
        //        GetUserResponseDto findUserResponseDto = userFeignClient.get(passport.getUserId());

        updateProducer(producer, updateProducerRequestDto);
        producer.updateModifiedData(1L);

        // 상품 전파 (업체 관련 정보 변동시에만)
        if (updateProducerRequestDto.getCompanyName() != null || updateProducerRequestDto.getCompanyType() != null || updateProducerRequestDto.getCompanyAddress() != null) {
            UpdateProductDto updateProductDto = UpdateProductDto.builder()
                                                                .producerName(producer.getUserName())
                                                                .producerAddress(producer.getCompanyAddress())
                                                                .producerId(producerId)
                                                                .build();

            producer.getProductInfos()
                    .forEach(productInfo -> productClient.update(updateProductDto, productInfo.getProductId()));
        }


    }


    public void updateProducer(
            Producer producer,
            UpdateProducerRequestDto dto
    ) {
        //TODO:: 데이터를 어떤것만 가져올지 정책적으로 정한 후에 코드 수정하기
        Optional.ofNullable(dto.getBusinessNumber())
                .ifPresent(producer::setBusinessNumber);
        Optional.ofNullable(dto.getCompanyName())
                .ifPresent(producer::setCompanyName);
        Optional.ofNullable(dto.getCompanyType())
                .ifPresent(producer::setCompanyType);
        Optional.ofNullable(dto.getCompanyAddress())
                .ifPresent(producer::setCompanyAddress);
        if (Optional.ofNullable(dto.getLatitude())
                    .isPresent() && Optional.ofNullable(dto.getLongitude())
                                            .isPresent()) {
            Point point = GeoUtil.createPoint(dto.getLatitude(), dto.getLongitude());
            producer.setLocation(point);
        }
        Optional.ofNullable(dto.getHubId())
                .ifPresent(producer::setHubId);
    }

    @Transactional
    public void delete(UUID producerId) {
        Producer producer = producerHelper.getProducer(producerId);

        //TODO :: 방법2. 생산업체는 null 처리하고 남은 모든 재고까지만 판매

            //1. 상품 삭제 처리 ->허브 상품쪽 알아서 이벤트 날아가므로 추가 작업 필요없음
            productClient.delete(producerId);
            // 모두 성공시 회사 softDelete
            producer.softDeleteData(1L);

    }
}
