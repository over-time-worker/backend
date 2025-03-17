package com.owlexpress.hub.presentation;

import com.owlexpress.hub.domain.service.HubService;
import com.owlexpress.hub.presentation.dto.CommonDto;
import com.owlexpress.hub.presentation.dto.request.HubCreateRequestDto;
import com.owlexpress.hub.presentation.dto.response.HubFindResponseDto;
import com.owlexpress.hub.presentation.dto.request.HubUpdateRequestDto;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/hub")
public class HubController {

    private final HubService hubService;

    @GetMapping("/{hubId}")
    public ResponseEntity<CommonDto<HubFindResponseDto>> find(@PathVariable("hubId") UUID hubId) {

        CommonDto<HubFindResponseDto> found = CommonDto.<HubFindResponseDto>builder()
                .status(HttpStatus.OK)
                .code(HttpStatus.OK.value())
                .message("허브 조회 성공")
                .data(hubService.find(hubId))
                .build();

        return ResponseEntity.ok(found);
    }

    @PostMapping
    public ResponseEntity<CommonDto<Void>> create(
            @Validated @RequestBody HubCreateRequestDto requestDto
    ) {
        hubService.create(requestDto);

        CommonDto<Void> created = CommonDto.<Void>builder()
                .status(HttpStatus.CREATED)
                .code(HttpStatus.CREATED.value())
                .message("허브 등록 완료")
                .data(null)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(created);
    }

    @PutMapping
    public ResponseEntity<CommonDto<Void>> update(
            @Validated @RequestBody HubUpdateRequestDto requestDto
    ) {
        hubService.update(requestDto);

        CommonDto<Void> updated = CommonDto.<Void>builder()
                .status(HttpStatus.ACCEPTED)
                .code(HttpStatus.ACCEPTED.value())
                .message("허브 수정 완료")
                .data(null)
                .build();

        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(updated);
    }

    @DeleteMapping
    public void delete() {
    }

}
