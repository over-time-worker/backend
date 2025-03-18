package com.owlexpress.product.application;

import com.owlexpress.product.application.dto.response.FindProductResponseDto;
import com.owlexpress.product.application.dto.response.SearchProductResponseDto;
import com.owlexpress.product.common.dto.CreateProductInfoRequestDto;
import com.owlexpress.product.common.dto.ProducerResponseDto;
import com.owlexpress.product.common.dto.UpdateProductInfoRequestDto;
import com.owlexpress.product.common.exceptions.ProductException;
import com.owlexpress.product.domain.entity.HubInfo;
import com.owlexpress.product.domain.entity.Product;
import com.owlexpress.product.domain.repository.ProductRepository;
import com.owlexpress.product.infrastructure.config.ProductSearchConfig;
import com.owlexpress.product.infrastructure.feignClient.ProducerClient;
import com.owlexpress.product.presentation.dto.request.CreateHubInfoRequestDto;
import com.owlexpress.product.presentation.dto.request.CreateProductRequestDto;
import com.owlexpress.product.presentation.dto.request.UpdateProductDto;
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


    public FindProductResponseDto find(UUID productsId) {
        Product product = getProduct(productsId);

        AtomicInteger totalQuantity = new AtomicInteger(1000);

        product.getHubInfo().forEach(
                hubInfo ->
                        //TODO :: Hub에 재고조회 API 날려서 각 재고 모아오기
                        totalQuantity.addAndGet(hubInfo.getHubProductQuantity())
        );
        return FindProductResponseDto.toDTO(product, totalQuantity);
    }

    @Transactional
    public void updateProduct(UpdateProductDto updateProductDto, UUID productsId) {
        //1.제품 정보 수정
        Product product = getProduct(productsId);

        product.setProductName(updateProductDto.getProductName());
        product.setProductPrice(updateProductDto.getProductPrice());
        product.setProductType(updateProductDto.getProductType());
        product.setProducerId(updateProductDto.getProducerId());
        product.setProducerName(updateProductDto.getProducerName());
        product.setProducerAddress(updateProductDto.getProducerAddress());

        //TODO :: AuditAware 추가 후 제거
        product.updateModifiedData(1L);

        //TODO :: 2. 제품 정보 수정 이벤트 전파(FeignClient)
        // - 생성업체 상품 정보에 전파
        UpdateProductInfoRequestDto updateProductInfoRequestDto = UpdateProductInfoRequestDto.fromEntity(product);
        producerClient.update(productsId,updateProductInfoRequestDto);
        // - 허브 상품 정보에 전파
        // - 장바구니에 전파
    }

    @Transactional
    public void delete(UUID productsId) {
        Product product = getProduct(productsId);

        //TODO:: 삭제 통신
        // 1.생산업체 물품 정보 삭제
        producerClient.delete(product.getProductId());
        // 2.허브 물품 정보 삭제

        //3. 장바구니에 있는 경우 삭제

        product.softDeleteData(1L);
        product.getHubInfo().forEach(
                hubInfo -> hubInfo.softDeleteData(1L)
        );

    }

    public PagedModel<SearchProductResponseDto> search(
            int page,
            int size,
            String sort,
            String q,
            String orderBy
    )
    {
        // 페이지 크기 제한 적용
        if (!productSearchConfig.getAllowedPageSizes().contains(size)) {
            size = productSearchConfig.getDefaultPageSize();
        }

        // 정렬 기준 제한 적용
        if (!productSearchConfig.getAllowedSorts().contains(sort)) {
            sort = productSearchConfig.getDefaultSort();
        }

        Sort.Direction direction = orderBy.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(direction, sort));

        Page<Product> productPage = productRepository.searchProducts(q, sort, orderBy, pageRequest
        );

        Page<SearchProductResponseDto> dtoPage = productPage.map(product -> {
            AtomicInteger totalQuantity = new AtomicInteger(0);

            // hubInfo에서 모든 허브의 제품 수량을 합산
            product.getHubInfo().forEach(hubInfo -> totalQuantity.addAndGet(hubInfo.getHubProductQuantity()));

            return SearchProductResponseDto.toDTO(product, totalQuantity);
        });

        return new PagedModel<>(dtoPage);
    }

    @Transactional
    public void connect(
            CreateHubInfoRequestDto createHubInfoRequestDto,
            HubInfo hubInfo
    )
    {
        Product product = getProduct(createHubInfoRequestDto.getProductId());
        product.getHubInfo().add(hubInfo);
        product  = productRepository.save(product);
        log.info(product.getHubInfo().toString());
        product.updateModifiedData(1L); //TODO :: AUdit설정후 삭제

        hubInfo.setProduct(product);
    }

    @Transactional
    public void createProduct(CreateProductRequestDto createProductRequestDto) {

        //1. 상품 중복검사
        validateProductName(createProductRequestDto);

        //2. producer 정보 조회
        ProducerResponseDto producerResponseDto = producerClient.find(createProductRequestDto.getProducerId())
                                                 .getData();

        validateProducerResponseDto(producerResponseDto);

        //3.상품 등록
        Product product = CreateProductRequestDto.toEntity(createProductRequestDto,producerResponseDto);
        //TODO :: AuditAware 추가 후 제거
        product.updateCreateData(1L);
        product = productRepository.save(product);

        //4. client 통신용 dto 생성
        CreateProductInfoRequestDto createProductInfoRequestDto = CreateProductInfoRequestDto.fromEntity(product);

        //5.상품 정보 Producer(productInfo)에 전달
        producerClient.create(createProductInfoRequestDto);


    }

    private static void validateProducerResponseDto(ProducerResponseDto producerResponseDto) {
        if(producerResponseDto == null) {
            throw new BadRequestException("요청 오류");//TODO:: CompanyName추가하고 null체크하기
        } else if (producerResponseDto.getCompanyAddress() ==null) {
            throw new BadRequestException("회사주소를 찾을 수 없습니다.");
        }
    }

    private void validateProductName(CreateProductRequestDto createProductRequestDto) {
        productRepository.findByProductName(createProductRequestDto.getProductName()).ifPresent(
                product -> {
                    throw new ProductException.ProductNameDuplicateExceptoin("해당 상품명이 이미 존재합니다.");
                }
        );
    }

    private Product getProduct(UUID productsId) {
        log.info("productsId: {}", productsId);
        return productRepository.findById(productsId).orElseThrow(
                () -> new ProductException.ProductNotFoundException("찾는 상품이 없습니다")
        );
    }

    @Transactional
    public void disConnect(HubInfo hubInfo) {
        Product product = hubInfo.getProduct();
        product.getHubInfo().remove(hubInfo);
        hubInfo.setProduct(null);
        productRepository.save(product);
    }
}
