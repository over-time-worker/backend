package com.owlexpress.delivery.presentation;

import com.owlexpress.delivery.application.dtos.CommonDto;
import com.owlexpress.delivery.application.dtos.request.DeliveryCompleteRequestDto;
import com.owlexpress.delivery.application.dtos.request.DeliveryCreateRequestDto;
import com.owlexpress.delivery.application.dtos.request.DeliveryUpdateRequestDto;
import com.owlexpress.delivery.application.dtos.response.DeliveryCreateResponseDto;
import com.owlexpress.delivery.application.dtos.response.DeliveryFindResponseDto;
import com.owlexpress.delivery.application.service.DeliveryService;
import com.owlexpress.delivery.common.helper.PassportHelper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static com.owlexpress.delivery.presentation.ApiResponseMessageConstant.*;

@RestController
@RequestMapping("/api/delivery")
@RequiredArgsConstructor
public class DeliveryController {

    private final DeliveryService deliveryService;
    private final PassportHelper passportHelper;

    @PostMapping()
    public ResponseEntity<CommonDto<DeliveryCreateResponseDto>> create(
            @RequestBody DeliveryCreateRequestDto deliveryCreateRequestDto,
            @RequestHeader("X-User-Passport") String passport
    ) {
        DeliveryCreateResponseDto response = deliveryService.create(deliveryCreateRequestDto, passport);

        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(CommonDto.<DeliveryCreateResponseDto>builder()
                                            .status(HttpStatus.CREATED)
                                            .message(CREATE_DELIVERY_SUCCESS)
                                            .code(HttpStatus.CREATED.value())
                                            .data(response)
                                            .build());
    }

    @PatchMapping("/{delivery_id}")
    public ResponseEntity<CommonDto<Void>> update(
            @PathVariable("delivery_id") UUID deliveryId,
            @RequestBody DeliveryUpdateRequestDto deliveryUpdateRequestDto,
            @RequestHeader("X-User-Passport") String passport
    ) {
        deliveryService.update(deliveryId, deliveryUpdateRequestDto, passport);

        return ResponseEntity.status(HttpStatus.ACCEPTED)
                             .body(CommonDto.<Void>builder()
                                            .status(HttpStatus.ACCEPTED)
                                            .message(UPDATE_DELIVERY_SUCCESS)
                                            .code(HttpStatus.ACCEPTED.value())
                                            .data(null)
                                            .build());
    }

    @DeleteMapping("/{delivery_id}")
    public ResponseEntity<CommonDto<Void>> delete(
            @PathVariable("delivery_id") UUID deliveryId,
            @RequestHeader("X-User-Passport") String passport
    ) {
        deliveryService.delete(deliveryId, passport);

        return ResponseEntity.status(HttpStatus.ACCEPTED)
                             .body(CommonDto.<Void>builder()
                                            .status(HttpStatus.ACCEPTED)
                                            .message(DELETE_DELIVERY_SUCCESS)
                                            .code(HttpStatus.ACCEPTED.value())
                                            .data(null)
                                            .build());
    }

    @GetMapping("/{delivery_id}")
    public ResponseEntity<CommonDto<DeliveryFindResponseDto>> find(
            @PathVariable("delivery_id") UUID deliveryId
    ) {
        DeliveryFindResponseDto response = deliveryService.find(deliveryId);

        return ResponseEntity.status(HttpStatus.OK)
                             .body(CommonDto.<DeliveryFindResponseDto>builder()
                                            .status(HttpStatus.OK)
                                            .message(GET_DELIVERY_SUCCESS)
                                            .code(HttpStatus.OK.value())
                                            .data(response)
                                            .build());
    }

    @GetMapping("/{consumer_id}/consumer")
    public ResponseEntity<CommonDto<Boolean>> findByConsumer(
            @PathVariable("consumer_id") UUID consumerId
    ) {
        Boolean flag = deliveryService.findByConsumer(consumerId);


        return ResponseEntity.status(HttpStatus.OK)
                             .body(CommonDto.<Boolean>builder()
                                            .status(HttpStatus.OK)
                                            .message(GET_DELIVERY_SUCCESS)
                                            .code(HttpStatus.OK.value())
                                            .data(flag)
                                            .build());
    }

    @GetMapping("/search")
    public ResponseEntity<CommonDto<PagedModel<DeliveryFindResponseDto>>> search(
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "sort", defaultValue = "CREATEDAT") String sort,
            @RequestParam(name = "order_by", defaultValue = "ASC") String orderBy,
            @RequestParam(name = "start_date", required = false) String startDate,
            @RequestParam(name = "end_date", required = false) String endDate,
            @RequestParam(name = "delivery_id", required = false) UUID deliveryId,
            @RequestParam(name = "delivery_status", required = false) String deliveryStatus
    ) {
        PagedModel<DeliveryFindResponseDto> response = deliveryService.search(page, size, sort, orderBy, startDate,
                                                                              endDate, deliveryId, deliveryStatus
        );

        return ResponseEntity.status(HttpStatus.OK)
                             .body(CommonDto.<PagedModel<DeliveryFindResponseDto>>builder()
                                            .status(HttpStatus.OK)
                                            .message(SEARCH_DELIVERY_SUCCESS)
                                            .code(HttpStatus.OK.value())
                                            .data(response)
                                            .build());
    }

    @PatchMapping("/hub/{delivery_id}/{delivery_history_id}")
    public ResponseEntity<CommonDto<Void>> startHubDelivery(
            @PathVariable("delivery_id") UUID deliveryId,
            @PathVariable("delivery_history_id") UUID deliveryHistoryId,
            @RequestHeader("X-User-Passport") String passport
    ) {
        deliveryService.startHubDelivery(deliveryId, deliveryHistoryId, passport);

        return ResponseEntity.status(HttpStatus.ACCEPTED)
                             .body(CommonDto.<Void>builder()
                                            .status(HttpStatus.ACCEPTED)
                                            .message(START_DELIVERY_SUCCESS)
                                            .code(HttpStatus.ACCEPTED.value())
                                            .data(null)
                                            .build());
    }

    @PatchMapping("/company/{delivery_id}/{delivery_history_id}")
    public ResponseEntity<CommonDto<Void>> startCompanyDelivery(
            @PathVariable("delivery_id") UUID deliveryId,
            @PathVariable("delivery_history_id") UUID deliveryHistoryId,
            @RequestHeader("X-User-Passport") String passport
    ) {
        deliveryService.startCompanyDelivery(deliveryId, deliveryHistoryId, passport);

        return ResponseEntity.status(HttpStatus.ACCEPTED)
                             .body(CommonDto.<Void>builder()
                                            .status(HttpStatus.ACCEPTED)
                                            .message(START_DELIVERY_SUCCESS)
                                            .code(HttpStatus.ACCEPTED.value())
                                            .data(null)
                                            .build());
    }

    @PatchMapping("/hub/{delivery_id}/{delivery_history_id}/complete")
    public ResponseEntity<CommonDto<Void>> completeHubDelivery(
            @PathVariable("delivery_id") UUID deliveryId,
            @PathVariable("delivery_history_id") UUID deliveryHistoryId,
            @RequestBody DeliveryCompleteRequestDto completeRequestDto,
            @RequestHeader("X-User-Passport") String passport
    ) {
        deliveryService.completeHubDelivery(deliveryId, deliveryHistoryId, completeRequestDto, passport);

        return ResponseEntity.status(HttpStatus.ACCEPTED)
                             .body(CommonDto.<Void>builder()
                                            .status(HttpStatus.ACCEPTED)
                                            .message(FINISH_DELIVERY_SUCCESS)
                                            .code(HttpStatus.ACCEPTED.value())
                                            .data(null)
                                            .build());
    }

    @PatchMapping("/company/{delivery_id}/{delivery_history_id}/complete")
    public ResponseEntity<CommonDto<Void>> completeCompanyDelivery(
            @PathVariable("delivery_id") UUID deliveryId,
            @PathVariable("delivery_history_id") UUID deliveryHistoryId,
            @RequestBody DeliveryCompleteRequestDto completeRequestDto,
            @RequestHeader("X-User-Passport") String passport
    ) {
        deliveryService.completeCompanyDelivery(deliveryId, deliveryHistoryId, completeRequestDto, passport);

        return ResponseEntity.status(HttpStatus.ACCEPTED)
                             .body(CommonDto.<Void>builder()
                                            .status(HttpStatus.ACCEPTED)
                                            .message(FINISH_DELIVERY_SUCCESS)
                                            .code(HttpStatus.ACCEPTED.value())
                                            .data(null)
                                            .build());
    }

    // TODO : 외부에서 요청 할 일 없을 것 같은데 비동기로 바꾸게 된다면 혹시 모르니 주석..
    //    @PostMapping("/delivery-history")
    //    public ResponseEntity<CommonDto<DeliveryHistoryCreateResponseDto>> createDeliveryHistory(
    //            @RequestBody DeliveryHistoryCreateRequestDto DeliveryHistoryCreateRequestDto
    //    ) {
    //        DeliveryHistoryCreateResponseDto response = deliveryService.createDeliveryHistory(DeliveryHistoryCreateRequestDto);
    //
    //        return ResponseEntity.status(HttpStatus.CREATED).body(
    //                CommonDto.<DeliveryHistoryCreateResponseDto>builder()
    //                        .status(HttpStatus.CREATED)
    //                        .message(CREATE_DELIVERY_HISTORY_SUCCESS)
    //                        .code(HttpStatus.CREATED.value())
    //                        .data(response)
    //                        .build());
    //    }

}
