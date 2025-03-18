package com.owlexpress.hub.presentation;

import com.owlexpress.hub.application.HubProductUseCase;
import com.owlexpress.hub.common.Constant.ResponseMessage;
import com.owlexpress.hub.domain.service.HubService;
import com.owlexpress.hub.presentation.dto.CommonDto;
import com.owlexpress.hub.presentation.dto.request.HubProductCreateRequestDto;
import com.owlexpress.hub.presentation.dto.request.HubProductUpdateRequestDto;
import com.owlexpress.hub.presentation.dto.response.HubProductFindResponseDto;
import com.owlexpress.hub.presentation.dto.response.HubProductSearchResponseDto;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/hub/product")
@RequiredArgsConstructor
public class HubProductController {

    private final HubService hubService;
    private final HubProductUseCase hubProductUseCase;

    @PostMapping
    public ResponseEntity<CommonDto<Void>> create(
            @RequestBody HubProductCreateRequestDto requestDto
    ) {
        hubProductUseCase.create(requestDto);
        CommonDto<Void> created = CommonDto.<Void>builder()
                .status(HttpStatus.CREATED)
                .code(HttpStatus.CREATED.value())
                .message(ResponseMessage.HUB_PRODUCT_CREATE_SUCCESS)
                .data(null)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping
    public ResponseEntity<CommonDto<HubProductUpdateRequestDto>> update(
            @RequestBody HubProductUpdateRequestDto requestDto
    ) {
        hubProductUseCase.update(requestDto);
        CommonDto<HubProductUpdateRequestDto> updated = CommonDto.<HubProductUpdateRequestDto>builder()
                .status(HttpStatus.ACCEPTED)
                .code(HttpStatus.ACCEPTED.value())
                .message(ResponseMessage.HUB_PRODUCT_UPDATE_SUCCESS)
                .data(null)
                .build();

        return ResponseEntity.ok().body(updated);
    }

    @GetMapping("/search")
    public ResponseEntity<CommonDto<PagedModel<HubProductSearchResponseDto>>> search(
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "10") Integer size,
            @RequestParam(name = "sort", required = false, defaultValue = "desc") String sort,
            @RequestParam(name = "orderBy", defaultValue = "createdAt") String orderBy,
            @RequestParam(name = "q", defaultValue = "") String q
    ) {
        PagedModel<HubProductSearchResponseDto> products = hubService.searchHubProduct(
                page, size, sort, q, orderBy);
        CommonDto<PagedModel<HubProductSearchResponseDto>> Searched =
                CommonDto.<PagedModel<HubProductSearchResponseDto>>builder()
                        .status(HttpStatus.CREATED)
                        .code(HttpStatus.CREATED.value())
                        .message(ResponseMessage.HUB_PRODUCT_SEARCH_SUCCESS)
                        .data(products)
                        .build();

        return ResponseEntity.ok().body(Searched);

    }

    @GetMapping("/{hubProductId}")
    public ResponseEntity<CommonDto<HubProductFindResponseDto>> find(
            @PathVariable("hubProductId") UUID hubProductId
    ) {
        HubProductFindResponseDto hubProduct = hubService.findHubProduct(hubProductId);

        CommonDto<HubProductFindResponseDto> found =
                CommonDto.<HubProductFindResponseDto>builder()
                        .status(HttpStatus.OK)
                        .code(HttpStatus.OK.value())
                        .message(ResponseMessage.HUB_PRODUCT_FIND_SUCCESS)
                        .data(hubProduct)
                        .build();

        return ResponseEntity.ok(found);
    }

    @DeleteMapping("/{hubProductId}")

    public ResponseEntity<CommonDto<Void>> delete(
            @PathVariable("hubProductId") UUID hubProductId
    ) {
        CommonDto<Void> deleted =
                CommonDto.<Void>builder()
                        .status(HttpStatus.OK)
                        .code(HttpStatus.OK.value())
                        .message("허브 상품 삭제 완료")
                        .data(null)
                        .build();

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(deleted);
    }
}
