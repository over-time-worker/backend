package com.owlexpress.order.presentation.controller;

import static com.owlexpress.order.presentation.dto.ApiResponseMessageConstant.CREATE_ORDER_SUCCESS_MESSAGE;
import static com.owlexpress.order.presentation.dto.ApiResponseMessageConstant.DELETE_ORDER_SUCCESS_MESSAGE;
import static com.owlexpress.order.presentation.dto.ApiResponseMessageConstant.FIND_ORDER_SUCCESS_MESSAGE;
import static com.owlexpress.order.presentation.dto.ApiResponseMessageConstant.SEARCH_ORDER_SUCCESS_MESSAGE;
import static com.owlexpress.order.presentation.dto.ApiResponseMessageConstant.UPDATE_ORDER_SUCCESS_MESSAGE;

import com.owlexpress.order.application.service.OrderService;
import com.owlexpress.order.common.dto.CommonDto;
import com.owlexpress.order.presentation.dto.request.CreateOrderRequestDto;
import com.owlexpress.order.presentation.dto.request.UpdateOrderRequestDto;
import com.owlexpress.order.presentation.dto.response.CreateOrderResponseDto;
import com.owlexpress.order.presentation.dto.response.GetOrderResponseDto;
import com.owlexpress.order.presentation.dto.response.OrderSearchResponseDto;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<CommonDto<CreateOrderResponseDto>> createOrder(
            @RequestHeader("X-User-Passport") String passport,
            @RequestBody CreateOrderRequestDto request
    ) {
        CreateOrderResponseDto responseDto = orderService.createOrder(passport, request);

        CommonDto<CreateOrderResponseDto> commonDto = CommonDto
                .<CreateOrderResponseDto>builder()
                .status(HttpStatus.CREATED)
                .code(HttpStatus.CREATED.value())
                .message(CREATE_ORDER_SUCCESS_MESSAGE)
                .data(responseDto)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(commonDto);
    }

    @GetMapping("/{order_id}")
    public ResponseEntity<CommonDto<GetOrderResponseDto>> findOrder(
            @RequestHeader("X-User-Passport") String passport,
            @PathVariable("order_id") UUID orderId
    ){
        GetOrderResponseDto response = orderService.findOrder(orderId, passport);

        CommonDto<GetOrderResponseDto> commonDto = CommonDto
                .<GetOrderResponseDto>builder()
                .status(HttpStatus.OK)
                .code(HttpStatus.OK.value())
                .message(FIND_ORDER_SUCCESS_MESSAGE)
                .data(response)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(commonDto);
    }

    @GetMapping("/search")
    public ResponseEntity<CommonDto<PagedModel<OrderSearchResponseDto>>> search(
            @RequestHeader("X-User-Passport") String passport,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "sort", defaultValue = "createdAt") String sort,
            @RequestParam(name = "order_by", defaultValue = "ASC") String orderBy,
            @RequestParam(name = "start_date", required = false) String startDate,
            @RequestParam(name = "end_date", required = false) String endDate
    ){
        PagedModel<OrderSearchResponseDto> response = orderService.search(
                passport,
                page,
                size,
                sort,
                orderBy,
                startDate,
                endDate
        );

        CommonDto<PagedModel<OrderSearchResponseDto>> commonDto = CommonDto
                .<PagedModel<OrderSearchResponseDto>>builder()
                .status(HttpStatus.OK)
                .code(HttpStatus.OK.value())
                .message(SEARCH_ORDER_SUCCESS_MESSAGE)
                .data(response)
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(commonDto);
    }

    @PatchMapping("/{order_id}")
    public ResponseEntity<CommonDto<Void>> update(
            @RequestHeader("X-User-Passport") String passport,
            @PathVariable("order_id") UUID orderId,
            @RequestBody UpdateOrderRequestDto requestDto
    ){

        orderService.updateOrder(orderId, requestDto, passport);

        CommonDto<Void> commonDto = CommonDto
                .<Void>builder()
                .status(HttpStatus.ACCEPTED)
                .code(HttpStatus.ACCEPTED.value())
                .message(UPDATE_ORDER_SUCCESS_MESSAGE)
                .data(null)
                .build();

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(commonDto);
    }

    @DeleteMapping("/{order_id}")
    public ResponseEntity<CommonDto<Void>> delete(
            @RequestHeader("X-User-Passport") String passport,
            @PathVariable("order_id") UUID orderId
    ){

        orderService.deleteOrder(orderId, passport);

        CommonDto<Void> commonDto = CommonDto
                .<Void>builder()
                .status(HttpStatus.ACCEPTED)
                .code(HttpStatus.ACCEPTED.value())
                .message(DELETE_ORDER_SUCCESS_MESSAGE)
                .data(null)
                .build();

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(commonDto);
    }
}
