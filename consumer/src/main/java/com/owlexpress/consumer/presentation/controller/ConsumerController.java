package com.owlexpress.consumer.presentation.controller;

import com.owlexpress.consumer.common.CommonDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/consumers")
public class ConsumerController {

    @GetMapping
    public ResponseEntity<CommonDto<Void>> create() {

        //메서드 넣기

        CommonDto<Void> commonDto = CommonDto.<Void>builder()
                                             .status(HttpStatus.CREATED)
                                             .code(HttpStatus.CREATED.value())
                                             .message("수령업체 생성 성공")
                                             .build();

        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(commonDto);
    }

    @PutMapping("/{consumerId}")
    public ResponseEntity<CommonDto<Void>> update(
            //TODO:: gateway 반환 유저 데이터 @RequestHeader("X-User-Passport") String passport,
            @PathVariable UUID consumerId
    ) {

        //메서드 넣기


        CommonDto<Void> commonDto = CommonDto.<Void>builder()
                                             .status(HttpStatus.ACCEPTED)
                                             .code(HttpStatus.ACCEPTED.value())
                                             .message("수령업체 수정 성공")
                                             .build();

        return ResponseEntity.status(HttpStatus.ACCEPTED)
                             .body(commonDto);
    }

    @GetMapping("/{productsId}")
    public ResponseEntity<CommonDto<?>> get(
            //TODO:: gateway 반환 유저 데이터 @RequestHeader("X-User-Passport") String passport,
            @PathVariable UUID consumerId
    ) {

        //메서드 넣기

        CommonDto<?> commonDto = CommonDto.<?>builder()
                                          .status(HttpStatus.OK)
                                          .code(HttpStatus.OK.value())
                                          .message("수령 업체 조회 성공")
                                          .data()
                                          .build();

        return ResponseEntity.status(HttpStatus.OK)
                             .body(commonDto);
    }

    @GetMapping("/search")
    public ResponseEntity<CommonDto<PagedModel<?>>> search(
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "10") Integer size,
            @RequestParam(name = "sort", required = false, defaultValue = "desc") String sort,
            @RequestParam(name = "q", defaultValue = "") String q,
            @RequestParam(name = "orderBy", defaultValue = "createdAt") String orderBy
    ) {

        CommonDto<PagedModel<?>> commonDto = CommonDto.<PagedModel<?>>builder()
                                                      .status(HttpStatus.OK)
                                                      .code(HttpStatus.OK.value())
                                                      .message("수령업체 검색 성공")
                                                      .data()
                                                      .build();

        return ResponseEntity.status(HttpStatus.OK)
                             .body(commonDto);
    }

    @DeleteMapping("/{consumerId}")
    public ResponseEntity<CommonDto<Void>> delete(
            @PathVariable UUID consumerId
    ) {


        CommonDto<Void> commonDto = CommonDto.<Void>builder()
                                             .status(HttpStatus.ACCEPTED)
                                             .code(HttpStatus.ACCEPTED.value())
                                             .message("수령업체 삭제 성공")
                                             .build();

        return ResponseEntity.status(HttpStatus.ACCEPTED)
                             .body(commonDto);
    }
}
