package com.owlexpress.auth.presentation;

import com.owlexpress.auth.application.dto.request.UserLoginRequestDto;

public interface AuthService {


    String signIn(UserLoginRequestDto userLoginRequestDto);
}
