package com.owlexpress.hub.presentation;

import com.owlexpress.hub.domain.service.HubService;
import com.owlexpress.hub.presentation.dto.CommonDto;
import com.owlexpress.hub.presentation.dto.HubCreateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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

    @GetMapping
    public void find() {
    }

    @PostMapping
    public ResponseEntity<CommonDto<Object>> create(
            @Validated @RequestBody HubCreateRequestDto requestDto
    ) {
        hubService.create(requestDto);

        CommonDto<Object> created = CommonDto.builder()
                .status(HttpStatus.CREATED)
                .code(HttpStatus.CREATED.value())
                .message("허브 등록 완료")
                .build();

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(created);
    }

    @PutMapping
    public void update() {
    }

    @DeleteMapping
    public void delete() {
    }

}
