package com.owlexpress.producer.application.usecase;

import com.owlexpress.producer.application.dto.request.UpdateProductDto;
import com.owlexpress.producer.common.dto.request.CreateProducerRequestDto;
import com.owlexpress.producer.common.dto.request.UpdateProducerRequestDto;
import com.owlexpress.producer.common.dto.response.GetUserInfoResponseDto;
import com.owlexpress.producer.common.dto.response.PassportDto;
import com.owlexpress.producer.common.exception.ProducerException;
import com.owlexpress.producer.common.helper.PassportHelper;
import com.owlexpress.producer.common.helper.ProducerHelper;
import com.owlexpress.producer.common.util.GeoUtil;
import com.owlexpress.producer.domain.entity.Producer;
import com.owlexpress.producer.domain.repository.ProducerRepository;
import com.owlexpress.producer.infrastructure.feignClient.ProductClient;
import com.owlexpress.producer.infrastructure.feignClient.UserFeignClient;
import com.owlexpress.producer.presentation.ProducerUsecase;
import feign.FeignException;
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
public class ProducerUsecaseImpl implements ProducerUsecase {

    private final UserFeignClient userFeignClient;
    private final ProducerRepository producerRepository;
    private final ProducerHelper producerHelper;
    private final ProductClient productClient;
    private final PassportHelper passportHelper;

    @Transactional
    @Override
    public void create(
            CreateProducerRequestDto createProducerRequestDto,
            String passport
    ) {
        PassportDto passportDto = passportHelper.getPassportDto(passport);

        GetUserInfoResponseDto getUserInfoResponseDto = userFeignClient.get(passport)
                .getData();

        producerRepository.findByCompanyName(createProducerRequestDto.getCompanyName())
                .ifPresent(producer -> {
                    throw new ProducerNameDuplicateException(PRODUCER_DUPLICATE_NAME_MESSAGE);
                });

        Producer producer = CreateProducerRequestDto.toEntity(createProducerRequestDto,
                getUserInfoResponseDto);
        producer.updateCreateData(passportDto.getUserId());

        producerRepository.save(producer);

    }

    @Transactional
    @Override
    public void update(
            UpdateProducerRequestDto updateProducerRequestDto,
            UUID producerId,
            String passport
    ) {
        PassportDto passportDto = passportHelper.getPassportDto(passport);
        Producer producer = producerHelper.getProducer(producerId);
        if (!producer.getUserId()
                .equals(passportDto.getUserId())) {
            throw new ProducerException.NotAuthorizedException(
                    PRODUCER_NOT_AUTHORIZED_UPDATE_MESSAGE);
        }

        updateProducer(producer, updateProducerRequestDto);
        producer.updateModifiedData(passportDto.getUserId());

        // 상품 전파 (업체 관련 정보 변동시에만)
        if (updateProducerRequestDto.getCompanyName() != null
                || updateProducerRequestDto.getCompanyType() != null
                || updateProducerRequestDto.getCompanyAddress() != null) {
            UpdateProductDto updateProductDto = UpdateProductDto.builder()
                    .producerName(producer.getUserName())
                    .producerAddress(producer.getCompanyAddress())
                    .producerId(producerId)
                    .build();

            producer.getProductInfos()
                    .forEach(productInfo -> productClient.update(passport, updateProductDto,
                            productInfo.getProductId()
                    ));
        }


    }


    @Override
    public void updateProducer(
            Producer producer,
            UpdateProducerRequestDto dto
    ) {
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
    @Override
    public void delete(
            UUID producerId,
            String passport
    ) {
        PassportDto passportDto = passportHelper.getPassportDto(passport);
        Producer producer = producerHelper.getProducer(producerId);

        try {
            if (productClient.delete(passport, producerId)
                    .getData()) {
                producer.softDeleteData(passportDto.getUserId());
            }
        } catch (FeignException.BadRequest badRequest) {
            producer.softDeleteData(passportDto.getUserId());
        }

    }


}
