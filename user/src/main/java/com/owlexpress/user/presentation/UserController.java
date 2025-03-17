package com.owlexpress.user.presentation;

import static com.owlexpress.user.presentation.dto.ApiResponseMessageConstant.DELETE_USER_SUCCESS;
import static com.owlexpress.user.presentation.dto.ApiResponseMessageConstant.GET_USER_INFO_SUCCESS;
import static com.owlexpress.user.presentation.dto.ApiResponseMessageConstant.SIGNIN_SUCCESS;
import static com.owlexpress.user.presentation.dto.ApiResponseMessageConstant.SIGNUP_SUCCESS;
import static com.owlexpress.user.presentation.dto.ApiResponseMessageConstant.UPDATE_USER_INFO_SUCCESS;
import static com.owlexpress.user.presentation.dto.ApiResponseMessageConstant.UPDATE_USER_PASSWORD_SUCCESS;

import com.owlexpress.user.application.service.UserService;
import com.owlexpress.user.common.CommonDto;
import com.owlexpress.user.presentation.dto.request.UpdatePasswordRequestDto;
import com.owlexpress.user.presentation.dto.request.UpdateUserInfoRequestDto;
import com.owlexpress.user.presentation.dto.request.UserSigninRequestDto;
import com.owlexpress.user.presentation.dto.request.UserSignupRequestDto;
import com.owlexpress.user.presentation.dto.response.GetUserInfoResponseDto;
import com.owlexpress.user.presentation.dto.response.UserSigninResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    @PostMapping("/sign-up")
    public ResponseEntity<CommonDto<Void>> signUp(
            @Valid @RequestBody UserSignupRequestDto userSignupRequestDto
    ) {
        userService.signup(userSignupRequestDto);

        CommonDto<Void> commonDto = CommonDto.<Void>builder()
                .status(HttpStatus.CREATED)
                .code(HttpStatus.CREATED.value())
                .message(SIGNUP_SUCCESS)
                .data(null)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(commonDto);
    }

    @PostMapping("/sign-in")
    public ResponseEntity<CommonDto<UserSigninResponseDto>> signIn(
            @Valid @RequestBody UserSigninRequestDto userSigninRequestDto
    ) {
        UserSigninResponseDto signin = userService.signin(userSigninRequestDto);

        CommonDto<UserSigninResponseDto> commonDto = CommonDto.<UserSigninResponseDto>builder()
                .status(HttpStatus.OK)
                .code(HttpStatus.OK.value())
                .message(SIGNIN_SUCCESS)
                .data(signin)
                .build();

        return ResponseEntity.ok().body(commonDto);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<CommonDto<GetUserInfoResponseDto>> get(
            //TODO:: gateway 반환 유저 데이터 @RequestHeader("X-User-Passport") String passport,
            @PathVariable("userId") Long userId
    ) {
        GetUserInfoResponseDto getUserInfoResponseDto = userService.find(userId);

        CommonDto<GetUserInfoResponseDto> commonDto = CommonDto.<GetUserInfoResponseDto>builder()
                .status(HttpStatus.OK)
                .code(HttpStatus.OK.value())
                .message(GET_USER_INFO_SUCCESS)
                .data(getUserInfoResponseDto)
                .build();

        return ResponseEntity.ok().body(commonDto);
    }

    @PatchMapping("/password")
    public ResponseEntity<CommonDto<Void>> patch(
            //TODO:: gateway 반환 유저 데이터 @RequestHeader("X-User-Passport") String passport,
            @Valid @RequestBody UpdatePasswordRequestDto updatePasswordRequestDto
    ) {
        // TODO : Passport 적용 이후 Service로 전달 후 UserId 변환
        Long userId = 1L;
        userService.changePassword(userId, updatePasswordRequestDto);

        CommonDto<Void> commonDto = CommonDto.<Void>builder()
                .status(HttpStatus.ACCEPTED)
                .code(HttpStatus.ACCEPTED.value())
                .message(UPDATE_USER_PASSWORD_SUCCESS)
                .data(null)
                .build();

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(commonDto);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<CommonDto<Void>> update(
            //TODO:: gateway 반환 유저 데이터 @RequestHeader("X-User-Passport") String passport,
            @PathVariable("userId") Long userId,
            @Valid @RequestBody UpdateUserInfoRequestDto updateUserInfoRequestDto
    ) {

        userService.updateUserInfo(userId, updateUserInfoRequestDto);

        CommonDto<Void> commonDto = CommonDto.<Void>builder()
                .status(HttpStatus.ACCEPTED)
                .code(HttpStatus.ACCEPTED.value())
                .message(UPDATE_USER_INFO_SUCCESS)
                .data(null)
                .build();

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(commonDto);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<CommonDto<Void>> delete(
            //TODO:: gateway 반환 유저 데이터 @RequestHeader("X-User-Passport") String passport,
            @PathVariable("userId") Long userId
    ) {
        userService.delete(userId);

        CommonDto<Void> commonDto = CommonDto.<Void>builder()
                .status(HttpStatus.ACCEPTED)
                .code(HttpStatus.ACCEPTED.value())
                .message(DELETE_USER_SUCCESS)
                .data(null)
                .build();

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(commonDto);
    }
}
