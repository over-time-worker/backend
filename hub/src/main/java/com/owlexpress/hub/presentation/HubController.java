package com.owlexpress.hub.presentation;

import com.owlexpress.hub.common.Constant.ResponseMessage;
import com.owlexpress.hub.domain.service.HubService;
import com.owlexpress.hub.presentation.dto.CommonDto;
import com.owlexpress.hub.presentation.dto.request.HubCreateRequestDto;
import com.owlexpress.hub.presentation.dto.request.HubUpdateRequestDto;
import com.owlexpress.hub.presentation.dto.response.HubFindResponseDto;
import com.owlexpress.hub.presentation.dto.response.HubSearchResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/hub")
//TODO:: passport 연결하기
public class HubController {

    private final HubService hubService;

    @GetMapping("/{hubId}")
    public ResponseEntity<CommonDto<HubFindResponseDto>> find(@PathVariable("hubId") UUID hubId) {

        CommonDto<HubFindResponseDto> found = CommonDto.<HubFindResponseDto>builder()
                .status(HttpStatus.OK)
                .code(HttpStatus.OK.value())
                .message(ResponseMessage.HUB_FIND_SUCCESS)
                .data(hubService.find(hubId))
                .build();

        return ResponseEntity.ok(found);
    }

    @PostMapping
    public ResponseEntity<CommonDto<Void>> create(
            @RequestBody HubCreateRequestDto requestDto
    ) {
        hubService.create(requestDto);

        CommonDto<Void> created = CommonDto.<Void>builder()
                .status(HttpStatus.CREATED)
                .code(HttpStatus.CREATED.value())
                .message(ResponseMessage.HUB_CREATE_SUCCESS)
                .data(null)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(created);
    }

    @PutMapping
    public ResponseEntity<CommonDto<Void>> update(
            @RequestBody HubUpdateRequestDto requestDto
    ) {
        hubService.update(requestDto);

        CommonDto<Void> updated = CommonDto.<Void>builder()
                .status(HttpStatus.ACCEPTED)
                .code(HttpStatus.ACCEPTED.value())
                .message(ResponseMessage.HUB_UPDATE_SUCCESS)
                .data(null)
                .build();

        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(updated);
    }

    @DeleteMapping("/{hubId}")
    public ResponseEntity<CommonDto<Void>> delete(
            @PathVariable("hubId") UUID hubId
    ) {

        hubService.delete(hubId);

        CommonDto<Void> commonDto = CommonDto.<Void>builder()
                                           .status(HttpStatus.ACCEPTED)
                                           .code(HttpStatus.ACCEPTED.value())
                                           .message(ResponseMessage.HUB_SEARCH_SUCCESS)
                                           .build();

        return ResponseEntity.ok(commonDto);
    }

    @GetMapping("/search")
    public ResponseEntity<CommonDto<PagedModel<HubSearchResponseDto>>> search(
            @RequestParam(name = "page", defaultValue = "0") Integer page,
            @RequestParam(name = "size", defaultValue = "10") Integer size,
            @RequestParam(name = "sort", required = false, defaultValue = "desc") String sort,
            @RequestParam(name = "q", defaultValue = "") String q,
            @RequestParam(name = "orderBy", defaultValue = "createdAt") String orderBy
    ) {
        PagedModel<HubSearchResponseDto> searchedResults = hubService.searchHub(page, size, sort, q,
                orderBy);
        CommonDto<PagedModel<HubSearchResponseDto>> searchCompleted = CommonDto.<PagedModel<HubSearchResponseDto>>builder()
                .status(HttpStatus.OK)
                .code(HttpStatus.OK.value())
                .message(ResponseMessage.HUB_SEARCH_SUCCESS)
                .data(searchedResults)
                .build();

        return ResponseEntity.ok(searchCompleted);
    }


}
