package com.owlexpress.auth.application;

import com.owlexpress.auth.application.dto.request.UserLoginRequestDto;
import com.owlexpress.auth.presentation.dto.request.LoginRequestDto;

public interface AuthService {


    String signIn(UserLoginRequestDto userLoginRequestDto);
}
