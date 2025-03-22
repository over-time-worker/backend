package com.owlexpress.product.application;

import com.owlexpress.product.application.dto.response.FindProductResponseDto;
import com.owlexpress.product.application.dto.response.SearchProductResponseDto;
import com.owlexpress.product.common.CommonDto;
import com.owlexpress.product.common.dto.request.CreateProductInfoRequestDto;
import com.owlexpress.product.common.dto.request.HubProductCreateRequestDto;
import com.owlexpress.product.common.dto.request.HubProductUpdateRequestDto;
import com.owlexpress.product.common.dto.request.UpdateProductInfoRequestDto;
import com.owlexpress.product.common.dto.response.HubProductFindResponseDto;
import com.owlexpress.product.common.dto.response.PassportDto;
import com.owlexpress.product.common.dto.response.ProducerResponseDto;
import com.owlexpress.product.common.exceptions.ProductException;
import com.owlexpress.product.common.helper.PassportHelper;
import com.owlexpress.product.domain.entity.HubInfo;
import com.owlexpress.product.domain.entity.Product;
import com.owlexpress.product.domain.repository.ProductRepository;
import com.owlexpress.product.infrastructure.config.ProductSearchConfig;
import com.owlexpress.product.infrastructure.feignClient.HubClient;
import com.owlexpress.product.infrastructure.feignClient.ProducerClient;
import com.owlexpress.product.presentation.dto.request.CreateHubInfoRequestDto;
import com.owlexpress.product.presentation.dto.request.CreateProductRequestDto;
import com.owlexpress.product.presentation.dto.request.UpdateProductDto;
import feign.FeignException;
import jakarta.ws.rs.BadRequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductUsecase {

    private final ProductRepository productRepository;
    private final ProductSearchConfig productSearchConfig; // 설정 클래스 주입
    private final ProducerClient producerClient;
    private final PassportHelper passportHelper;
    private final HubClient hubClient;

    @Transactional
    public void createProduct(
            CreateProductRequestDto createProductRequestDto,
            String passport
    ) {
        PassportDto passportDto = passportHelper.getPassportDto(passport);

        //1. 상품 중복검사
        validateProductName(createProductRequestDto);

        //2. producer 정보 조회
        ProducerResponseDto producerResponseDto = producerClient.find(createProductRequestDto.getProducerId())
                                                                .getData();
        log.info(producerResponseDto.toString());
        //3. 동일한 사업자인지 확인
        validateProducerResponseDto(producerResponseDto);

        //3.상품 등록
        Product product = CreateProductRequestDto.toEntity(createProductRequestDto, producerResponseDto);

        //4. 로그용 기록
        product.updateCreateData(passportDto.getUserId());

        //5. 상품 저장
        product = productRepository.save(product);

        //5. client 통신용 dto 생성
        CreateProductInfoRequestDto createProductInfoRequestDto = CreateProductInfoRequestDto.fromEntity(product);

        //5.상품 정보 productInfo Controller에 전달
        producerClient.create(passport,createProductInfoRequestDto);

        //6. 상품 관리 허브에 전달
        //producer조회해서 hubID가져오기
        try {
            CommonDto<ProducerResponseDto> producerResponseDtoCommonDto = producerClient.find(
                    createProductInfoRequestDto.getProducerId());

            log.info("!!!!!!!!!!!!!!!!!!={}",String.valueOf(product.getProducerId()));
            hubClient.create(passport, HubProductCreateRequestDto.toDto(
                    product, producerResponseDtoCommonDto.getData().getHubId()));

        } catch (Exception e) {
            producerClient.delete(passport, createProductInfoRequestDto.getProductId());
        }

    }

    @Transactional
    public void updateProduct(
            UpdateProductDto updateProductDto,
            UUID productsId,
            String passport
    ) {
        PassportDto passportDto = passportHelper.getPassportDto(passport);
        //1.제품 정보 수정
        Product product = getProduct(productsId);

        //2.Entity Update
        UpdateProductDto.updateProduct(product, updateProductDto);

        //3. 감사 기록 업데이트
        product.updateModifiedData(passportDto.getUserId());

        //4. 통신용 dto 생성
        UpdateProductInfoRequestDto updateProductInfoRequestDto = UpdateProductInfoRequestDto.fromEntity(product);

        //5. 생산업체 이벤트 전달
        producerClient.update(passport,productsId, updateProductInfoRequestDto);

        //6. 허브 상품 정보 이벤트 전달
        //허브의 상품 정보 조회
        HubProductFindResponseDto hubProductFindResponseDto = hubClient.findHubProduct(productsId)
                                                                       .getData();

        //허브 상품 업데이트 이벤트 전달
        hubClient.update(passport, HubProductUpdateRequestDto.toDto(hubProductFindResponseDto, updateProductDto));

    }

    @Transactional(readOnly = true)
    public FindProductResponseDto find(UUID productsId) {
        Product product = getProduct(productsId);

        AtomicInteger totalQuantity = new AtomicInteger(1000);

        product.getHubInfo()
               .forEach(hubInfo ->
                                //TODO :: Hub에 재고조회 API 날려서 각 재고 모아오기?
                                totalQuantity.addAndGet(hubInfo.getHubProductQuantity()));
        return FindProductResponseDto.toDTO(product, totalQuantity);
    }

    @Transactional
    public void delete(UUID productsId,
                       String passport
    ) {
        PassportDto passportDto = passportHelper.getPassportDto(passport);
        Product product = getProduct(productsId);

        product.softDeleteData(passportDto.getUserId());
        product.getHubInfo()
               .forEach(hubInfo -> hubInfo.softDeleteData(passportDto.getUserId()));

    }

    @Transactional(readOnly = true)
    public PagedModel<SearchProductResponseDto> search(
            int page,
            int size,
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
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(direction, sort));

        Page<Product> productPage = productRepository.searchProducts(q, sort, orderBy, pageRequest);

        Page<SearchProductResponseDto> dtoPage = productPage.map(product -> {
            AtomicInteger totalQuantity = new AtomicInteger(0);

            // hubInfo에서 모든 허브의 제품 수량을 합산
            product.getHubInfo()
                   .forEach(hubInfo -> totalQuantity.addAndGet(hubInfo.getHubProductQuantity()));

            return SearchProductResponseDto.toDTO(product, totalQuantity);
        });

        return new PagedModel<>(dtoPage);
    }

    @Transactional
    public void connect(
            CreateHubInfoRequestDto createHubInfoRequestDto,
            HubInfo hubInfo
    ) {
        Product product = getProduct(createHubInfoRequestDto.getProductId());
        product.getHubInfo()
               .add(hubInfo);
        product = productRepository.save(product);
        log.info(product.getHubInfo()
                        .toString());
        product.updateModifiedData(1L);
        hubInfo.setProduct(product);
    }

    private static void validateProducerResponseDto(ProducerResponseDto producerResponseDto) {
        if (producerResponseDto == null) {
            throw new BadRequestException("요청 오류");
        } else if (producerResponseDto.getCompanyName() == null) {
            throw new BadRequestException("회사주소를 찾을 수 없습니다.");
        }
    }

    private void validateProductName(CreateProductRequestDto createProductRequestDto) {
        productRepository.findByProductName(createProductRequestDto.getProductName())
                         .ifPresent(product -> {
                             throw new ProductException.ProductNameDuplicateExceptoin("해당 상품명이 이미 존재합니다.");
                         });
    }

    private Product getProduct(UUID productsId) {
        return productRepository.findById(productsId)
                                .orElseThrow(() -> new ProductException.ProductNotFoundException("찾는 상품이 없습니다"));
    }

    @Transactional
    public void disConnect(HubInfo hubInfo) {
        Product product = hubInfo.getProduct();
        product.getHubInfo()
               .remove(hubInfo);
        hubInfo.setProduct(null);
        productRepository.save(product);
    }
}
