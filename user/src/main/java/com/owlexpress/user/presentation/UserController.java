package com.owlexpress.user.presentation;

import static com.owlexpress.user.presentation.dto.ApiResponseMessageConstant.GET_USER_INFO_SUCCESS;
import static com.owlexpress.user.presentation.dto.ApiResponseMessageConstant.SIGNIN_SUCCESS;
import static com.owlexpress.user.presentation.dto.ApiResponseMessageConstant.SIGNUP_SUCCESS;
import static com.owlexpress.user.presentation.dto.ApiResponseMessageConstant.UPDATE_USER_PASSWORD_SUCCESS;

import com.owlexpress.user.application.service.UserService;
import com.owlexpress.user.common.CommonDto;
import com.owlexpress.user.presentation.dto.request.UpdatePasswordRequestDto;
import com.owlexpress.user.presentation.dto.request.UserSigninRequestDto;
import com.owlexpress.user.presentation.dto.request.UserSignupRequestDto;
import com.owlexpress.user.presentation.dto.response.GetUserInfoResponseDto;
import com.owlexpress.user.presentation.dto.response.UserSigninResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    @PostMapping("/sign-up")
    public ResponseEntity<CommonDto<Void>> signUp(
            // TODO : FeignClient 연결 후 @Valid 검증
            @RequestBody UserSignupRequestDto userSignupRequestDto
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
            // TODO : FeignClient 연결 후 @Valid 검증
            @RequestBody UserSigninRequestDto userSigninRequestDto
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
            @RequestHeader("X-User-Passport") String passport
    ) {
        GetUserInfoResponseDto getUserInfoResponseDto = userService.find(passport);

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
            @RequestHeader("X-User-Passport") String passport,
            @RequestBody UpdatePasswordRequestDto updatePasswordRequestDto
    ) {

        userService.changePassword(passport, updatePasswordRequestDto);

        CommonDto<Void> commonDto = CommonDto.<Void>builder()
                .status(HttpStatus.ACCEPTED)
                .code(HttpStatus.ACCEPTED.value())
                .message(UPDATE_USER_PASSWORD_SUCCESS)
                .data(null)
                .build();

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(commonDto);
    }
}
