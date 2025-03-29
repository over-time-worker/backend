package com.owlexpress.hub.presentation;

import com.owlexpress.hub.application.dto.response.HubProductIsEnoughResponseDto;
import com.owlexpress.hub.common.Constant.ResponseMessage;
import com.owlexpress.hub.domain.service.HubProductService;
import com.owlexpress.hub.domain.service.HubService;
import com.owlexpress.hub.presentation.dto.CommonDto;
import com.owlexpress.hub.presentation.dto.request.HubProductCheckRequestDto;
import com.owlexpress.hub.presentation.dto.request.HubProductCreateRequestDto;
import com.owlexpress.hub.presentation.dto.request.HubProductUpdateRequestDto;
import com.owlexpress.hub.presentation.dto.request.OrderConfirmRequestDto;
import com.owlexpress.hub.presentation.dto.response.HubProductFindResponseDto;
import com.owlexpress.hub.presentation.dto.response.HubProductOrderConfirmResponseDto;
import com.owlexpress.hub.presentation.dto.response.HubProductSearchResponseDto;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/hub/product")
@RequiredArgsConstructor
@Slf4j
public class HubProductController {

    private final HubService hubService;
    private final HubProductService hubProductService;
    private final HubProductUseCase hubProductUseCase;

    @PostMapping
    public ResponseEntity<CommonDto<Void>> create(
            @RequestHeader("X-User-Passport") String passport,
            @RequestBody HubProductCreateRequestDto requestDto
    ) {
        hubProductUseCase.create(requestDto, passport);
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
            @RequestHeader("X-User-Passport") String passport,
            @RequestBody HubProductUpdateRequestDto requestDto
    ) {
        hubProductService.update(requestDto, passport);
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
        PagedModel<HubProductSearchResponseDto> products = hubProductService.searchHubProduct(
                page, size, sort, q, orderBy);
        CommonDto<PagedModel<HubProductSearchResponseDto>> Searched =
                CommonDto.<PagedModel<HubProductSearchResponseDto>>builder()
                        .status(HttpStatus.OK)
                        .code(HttpStatus.OK.value())
                        .message(ResponseMessage.HUB_PRODUCT_SEARCH_SUCCESS)
                        .data(products)
                        .build();

        return ResponseEntity.ok().body(Searched);

    }

    @GetMapping("/{hubProductId}")
    public ResponseEntity<CommonDto<HubProductFindResponseDto>> find(
            @PathVariable("hubProductId") UUID hubProductId
    ) {
        HubProductFindResponseDto hubProduct = hubProductService.findHubProduct(hubProductId);

        CommonDto<HubProductFindResponseDto> found =
                CommonDto.<HubProductFindResponseDto>builder()
                        .status(HttpStatus.OK)
                        .code(HttpStatus.OK.value())
                        .message(ResponseMessage.HUB_PRODUCT_FIND_SUCCESS)
                        .data(hubProduct)
                        .build();

        return ResponseEntity.ok(found);
    }

    @GetMapping("/{productId}/product")
    public ResponseEntity<CommonDto<HubProductFindResponseDto>> findHubProduct(
            @PathVariable("productId") UUID productId
    ) {
        HubProductFindResponseDto hubProduct = hubProductService.findHubProductByProductId(
                productId);

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
            @RequestHeader("X-User-Passport") String passport,
            @PathVariable("hubProductId") UUID hubProductId
    ) {
        hubProductUseCase.delete(hubProductId, passport);
        CommonDto<Void> deleted =
                CommonDto.<Void>builder()
                        .status(HttpStatus.ACCEPTED)
                        .code(HttpStatus.ACCEPTED.value())
                        .message(ResponseMessage.HUB_PRODUCT_DELETE_SUCCESS)
                        .data(null)
                        .build();

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(deleted);
    }

    @PostMapping("/check-stock")
    public ResponseEntity<CommonDto<List<HubProductIsEnoughResponseDto>>> checkStock(
            @RequestBody List<HubProductCheckRequestDto> requestDto
    ) {
        List<HubProductIsEnoughResponseDto> isOrderPossibleByProductId =
                hubProductService.checkHubProductStocks(requestDto);

        CommonDto<List<HubProductIsEnoughResponseDto>> found =
                CommonDto.<List<HubProductIsEnoughResponseDto>>builder()
                        .status(HttpStatus.OK)
                        .code(HttpStatus.OK.value())
                        .message(ResponseMessage.HUB_PRODUCT_STOCK_CHECK_SUCCESS)
                        .data(isOrderPossibleByProductId)
                        .build();

        return ResponseEntity.status(HttpStatus.OK).body(found);
    }

    @PostMapping("/confirm-stock")
    public ResponseEntity<CommonDto<HubProductOrderConfirmResponseDto>> confirmStock(
            @RequestBody OrderConfirmRequestDto requestDto
    ) {
        HubProductOrderConfirmResponseDto confirmResponseDto = hubProductUseCase.confirmOrder(
                requestDto);
        CommonDto<HubProductOrderConfirmResponseDto> found =
                CommonDto.<HubProductOrderConfirmResponseDto>builder()
                        .status(HttpStatus.ACCEPTED)
                        .code(HttpStatus.ACCEPTED.value())
                        .message(ResponseMessage.HUB_PRODUCT_ORDER_SUCCESS)
                        .data(confirmResponseDto)
                        .build();

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(found);
    }

    @PostMapping("/rollback-stock")
    public ResponseEntity<CommonDto<Void>> rollbackOrder(Map<String, UUID> orderId) {
        hubProductUseCase.rollbackConfirmOrder(orderId.get("orderId"));

        CommonDto<Void> rollbacked = CommonDto.<Void>builder()
                .status(HttpStatus.ACCEPTED)
                .code(HttpStatus.ACCEPTED.value())
                .message("롤백 완료")
                .build();

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(rollbacked);

    }
}
