package com.owlexpress.product.presentation;

import com.owlexpress.product.application.ProductHubUsecase;
import com.owlexpress.product.common.CommonDto;
import com.owlexpress.product.domain.entity.HubInfo;
import com.owlexpress.product.domain.service.HubInfoService;
import com.owlexpress.product.presentation.dto.request.CreateHubInfoRequestDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class HubInfoController {

    private final HubInfoService hubInfoService;
    private final ProductHubUsecase productHubUsecase;

    @PostMapping("/hub-info")
    public ResponseEntity<CommonDto<Object>> create(
        //TODO:: gateway 반환 유저 데이터 @RequestHeader("X-User-Passport") String passport,
         @Valid @RequestBody CreateHubInfoRequestDto createHubInfoRequestDto
    ) {
        System.out.println("productId = "+ createHubInfoRequestDto.getProductId());
        System.out.println("hubId = "+ createHubInfoRequestDto.getHubId());
        HubInfo hubInfo = hubInfoService.create(createHubInfoRequestDto);
        productHubUsecase.connect(createHubInfoRequestDto,hubInfo);

        CommonDto<Object> commonDto = CommonDto.builder()
                .status(HttpStatus.CREATED)
                .code(HttpStatus.CREATED.value())
                .message("상품 생성 성공")
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(commonDto);
    }

}
