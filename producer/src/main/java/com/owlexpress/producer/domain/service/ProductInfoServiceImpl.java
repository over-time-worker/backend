package com.owlexpress.producer.domain.service;

import com.owlexpress.producer.common.exception.ProductInfoException;
import com.owlexpress.producer.common.helper.ProducerHelper;
import com.owlexpress.producer.domain.entity.Producer;
import com.owlexpress.producer.domain.entity.ProductInfo;
import com.owlexpress.producer.domain.repository.ProducerRepository;
import com.owlexpress.producer.domain.repository.ProductInfoRepository;
import com.owlexpress.producer.presentation.dto.request.CreateProductInfoRequestDto;
import com.owlexpress.producer.presentation.dto.request.UpdateProductInfoRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static com.owlexpress.producer.common.exception.ProductInfoException.ProductInfoNameDuplicateExceptoin;

@Service
@RequiredArgsConstructor
public class ProductInfoServiceImpl implements ProductInfoService {
    private final ProductInfoRepository productInfoRepository;
    private final ProducerRepository producerRepository;
    private final ProducerHelper producerHelper;

    @Override
    @Transactional
    public void create(CreateProductInfoRequestDto createProductInfoRequestDto) {

        //1. 상품명 중복 검사
        productInfoRepository.findByProductNmae(createProductInfoRequestDto.getProductName())
                             .ifPresent(productInfo -> {
                                 throw new ProductInfoNameDuplicateExceptoin("동일한 상품 정보명이 존재합니다.");
                             });

        //2. 생산 업체 조회
        Producer producer = producerHelper.getProducer(createProductInfoRequestDto.getProducerId());

        //3. 상품 정보 - 생산 업체 연결
        ProductInfo productInfo = CreateProductInfoRequestDto.toEntity(
                createProductInfoRequestDto,
                producer
        );
        productInfo.updateCreateData(1L); //TODO:: AuditAware후 삭제

        producer.getProductInfos()
                .add(productInfo);

        //4. 상품 정보 저장
        producerRepository.save(producer);

        productInfoRepository.save(productInfo);
    }

    @Override
    @Transactional
    public void update(
            UUID productId,
            UpdateProductInfoRequestDto updateProductInfoRequestDto
    ) {
        ProductInfo productInfo = getProductInfo(productId);
        productInfo.setProductName(updateProductInfoRequestDto.getProductName());
        productInfo.setProductPrice(updateProductInfoRequestDto.getProductPrice());
        productInfo.setProductType(updateProductInfoRequestDto.getProductType());

        productInfo.updateModifiedData(1L);
    }

    private ProductInfo getProductInfo(UUID productId) {
        return productInfoRepository.findByProductId(productId)
                                    .orElseThrow(() -> new ProductInfoException.ProductInfoNotFoundException("찾는 상품 정보가 없습니다."));
    }

    @Override
    @Transactional
    public void delete(UUID productId) {
        ProductInfo productInfo = getProductInfo(productId);

        productInfo.softDeleteData(1L);


    }
}
