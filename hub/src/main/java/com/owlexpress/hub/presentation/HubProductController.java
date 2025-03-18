package com.owlexpress.hub.presentation;

import com.owlexpress.hub.application.HubProductUseCase;
import com.owlexpress.hub.domain.service.HubProductService;
import com.owlexpress.hub.domain.service.HubService;
import com.owlexpress.hub.presentation.dto.CommonDto;
import com.owlexpress.hub.presentation.dto.request.HubProductCreateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/hub/product")
@RequiredArgsConstructor
public class HubProductController {

    private final HubProductService hubProductService;
    private final HubProductUseCase hubProductUseCase;
    @PostMapping
    public ResponseEntity<CommonDto<Void>> create(
            @RequestBody HubProductCreateRequestDto requestDto
    ) {
        hubProductUseCase.create(requestDto);
        CommonDto<Void> created = CommonDto.<Void>builder()
                .status(HttpStatus.CREATED)
                .code(HttpStatus.CREATED.value())
                .message("허브 상품 추가 성공")
                .data(null)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
}
