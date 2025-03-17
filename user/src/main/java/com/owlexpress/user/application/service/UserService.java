package com.owlexpress.user.application.service;

import com.owlexpress.user.presentation.dto.request.UpdatePasswordRequestDto;
import com.owlexpress.user.presentation.dto.request.UserSigninRequestDto;
import com.owlexpress.user.presentation.dto.request.UserSignupRequestDto;
import com.owlexpress.user.presentation.dto.response.GetUserInfoResponseDto;
import com.owlexpress.user.presentation.dto.response.UserSigninResponseDto;

public interface UserService {
    void signup (UserSignupRequestDto userSignupRequestDto);

    UserSigninResponseDto signin (UserSigninRequestDto userSigninRequestDto);

    GetUserInfoResponseDto find (Long userId);

    void changePassword (Long userId, UpdatePasswordRequestDto updatePasswordRequestDto);

}
