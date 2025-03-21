package com.owlexpress.user.presentation;

import static com.owlexpress.user.presentation.dto.ApiResponseMessageConstant.DELETE_USER_SUCCESS;
import static com.owlexpress.user.presentation.dto.ApiResponseMessageConstant.UPDATE_USER_INFO_SUCCESS;
import static com.owlexpress.user.presentation.dto.ApiResponseMessageConstant.USER_APPROVE_SUCCESS;
import static com.owlexpress.user.presentation.dto.ApiResponseMessageConstant.USER_ROLE_UPDATE_SUCCESS;
import static com.owlexpress.user.presentation.dto.ApiResponseMessageConstant.USER_SEARCH_SUCCESS;

import com.owlexpress.user.application.service.MasterService;
import com.owlexpress.user.common.CommonDto;
import com.owlexpress.user.presentation.dto.request.ApprovalUserRequestDto;
import com.owlexpress.user.presentation.dto.request.UpdateUserInfoRequestDto;
import com.owlexpress.user.presentation.dto.request.UpdateUserRoleRequestDto;
import com.owlexpress.user.presentation.dto.response.GetUsersResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/master/users")
public class MasterController {
    private final MasterService masterService;

    @PatchMapping("/role")
    public ResponseEntity<CommonDto<Void>> changeRole(
            @RequestBody UpdateUserRoleRequestDto updateUserRoleRequestDto
    ){
        masterService.updateRole(updateUserRoleRequestDto);

        CommonDto<Void> commonDto = CommonDto.<Void>builder()
                .status(HttpStatus.ACCEPTED)
                .code(HttpStatus.ACCEPTED.value())
                .message(USER_ROLE_UPDATE_SUCCESS)
                .data(null)
                .build();

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(commonDto);
    }

    @PatchMapping("/approve")
    public ResponseEntity<CommonDto<Void>> approve(
            @RequestHeader("X-User-Passport") String passport,
            @RequestBody ApprovalUserRequestDto approvalUserRequestDto
    ) {

        masterService.approvalUser(approvalUserRequestDto);

        CommonDto<Void> commonDto = CommonDto.<Void>builder()
                .status(HttpStatus.ACCEPTED)
                .code(HttpStatus.ACCEPTED.value())
                .message(USER_APPROVE_SUCCESS)
                .data(null)
                .build();

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(commonDto);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<CommonDto<Void>> delete(
            @RequestHeader("X-User-Passport") String passport
    ) {
        masterService.delete(passport);

        CommonDto<Void> commonDto = CommonDto.<Void>builder()
                .status(HttpStatus.ACCEPTED)
                .code(HttpStatus.ACCEPTED.value())
                .message(DELETE_USER_SUCCESS)
                .data(null)
                .build();

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(commonDto);
    }

    @PutMapping
    public ResponseEntity<CommonDto<Void>> update(
            // TODO : FeignClient 연결 후 @Valid 검증
            @RequestHeader("X-User-Passport") String passport,
            @RequestBody UpdateUserInfoRequestDto updateUserInfoRequestDto
    ) {

        masterService.updateUserInfo(updateUserInfoRequestDto);

        CommonDto<Void> commonDto = CommonDto.<Void>builder()
                .status(HttpStatus.ACCEPTED)
                .code(HttpStatus.ACCEPTED.value())
                .message(UPDATE_USER_INFO_SUCCESS)
                .data(null)
                .build();

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(commonDto);
    }

    @GetMapping("/search")
    public ResponseEntity<CommonDto<PagedModel<GetUsersResponseDto>>> find(
            @RequestParam(name = "q", required = false) String keyword,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            @RequestParam(name = "order_by", defaultValue = "createdAt") String orderBy,
            @RequestParam(name = "sort", defaultValue = "ASC" ,required = false) String sort
    ) {
        PagedModel<GetUsersResponseDto> pagedModel = masterService
                .searchUsers(keyword, page, size, orderBy, sort);

        CommonDto<PagedModel<GetUsersResponseDto>> commonDto = CommonDto
                .<PagedModel<GetUsersResponseDto>>builder()
                .status(HttpStatus.OK)
                .code(HttpStatus.OK.value())
                .message(USER_SEARCH_SUCCESS)
                .data(pagedModel)
                .build();

        return ResponseEntity.ok().body(commonDto);
    }
}