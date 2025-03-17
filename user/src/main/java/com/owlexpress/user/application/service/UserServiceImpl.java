package com.owlexpress.user.application.service;

import static com.owlexpress.user.common.exception.ExceptionMessage.NEW_PASSWORD_NOT_MATCHES_MESSAGE;
import static com.owlexpress.user.common.exception.ExceptionMessage.PASSWORD_INCORRECT_MESSAGE;
import static com.owlexpress.user.common.exception.ExceptionMessage.USER_NOT_FOUND_MESSAGE;

import com.owlexpress.user.application.exception.NewPasswordNotMatchesException;
import com.owlexpress.user.application.exception.PasswordIncorrectException;
import com.owlexpress.user.domain.entity.User;
import com.owlexpress.user.domain.repository.UserRepository;
import com.owlexpress.user.infrastructure.exception.UserNotFoundException;
import com.owlexpress.user.presentation.dto.request.UpdatePasswordRequestDto;
import com.owlexpress.user.presentation.dto.request.UpdateUserInfoRequestDto;
import com.owlexpress.user.presentation.dto.request.UserSigninRequestDto;
import com.owlexpress.user.presentation.dto.request.UserSignupRequestDto;
import com.owlexpress.user.presentation.dto.response.GetUserInfoResponseDto;
import com.owlexpress.user.presentation.dto.response.UserSigninResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void signup(UserSignupRequestDto userSignupRequestDto) {
        User user = User.userBuilder()
                        .accountId(userSignupRequestDto.getAccountId())
                        .password(passwordEncoder.encode(userSignupRequestDto.getPassword()))
                        .username(userSignupRequestDto.getUsername())
                        .phoneNumber(userSignupRequestDto.getPhoneNumber())
                        .platformId(userSignupRequestDto.getPlatformId())
                        .platformType(userSignupRequestDto.getPlatformType())
                        .isPublic(userSignupRequestDto.getIsPublic())
                        .role(userSignupRequestDto.getRole())
                        .build();

        userRepository.save(user);
    }

    @Override
    public UserSigninResponseDto signin(UserSigninRequestDto userSigninRequestDto) {
        User user = userRepository.findByAccountId(userSigninRequestDto.getAccountId())
                .orElseThrow(()-> new UserNotFoundException(USER_NOT_FOUND_MESSAGE));

        boolean matches = passwordEncoder.matches(
                user.getPassword(),
                userSigninRequestDto.getPassword()
        );

        if (!matches){
            throw new PasswordIncorrectException(PASSWORD_INCORRECT_MESSAGE);
        } else {
            return UserSigninResponseDto.toDto(user);
        }
    }

    @Override
    public GetUserInfoResponseDto find(Long userId) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(()-> new UserNotFoundException(USER_NOT_FOUND_MESSAGE));

        return GetUserInfoResponseDto.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .platformId(user.getPlatformId())
                .platformType(user.getPlatformType())
                .phoneNumber(user.getPhoneNumber())
                .role(user.getRole())
                .isPublic(user.getIsPublic())
                .build();
    }

    @Override
    public void changePassword(Long userId, UpdatePasswordRequestDto updatePasswordRequestDto) {
        if (!newPasswordMatching(updatePasswordRequestDto)){
            throw new NewPasswordNotMatchesException(NEW_PASSWORD_NOT_MATCHES_MESSAGE);
        }

        User user = userRepository.findByUserId(userId)
                .orElseThrow(()-> new UserNotFoundException(USER_NOT_FOUND_MESSAGE));

        boolean matches = passwordEncoder.matches(
                user.getPassword(),
                updatePasswordRequestDto.getOldPassword()
        );

        if (!matches) {
            throw new PasswordIncorrectException(PASSWORD_INCORRECT_MESSAGE);
        }

        user.setNewPassword(
                passwordEncoder.encode(updatePasswordRequestDto.getNewPassword()),
                userId
        );
    }

    @Override
    public void updateUserInfo(Long userId, UpdateUserInfoRequestDto updateUserInfoRequestDto) {
        // TODO : 수정 권한은 MASTER에게만 존재한다.
        User user = userRepository.findByUserId(userId)
                .orElseThrow(()-> new UserNotFoundException(USER_NOT_FOUND_MESSAGE));

        user.setUserInfo(
                userId,
                updateUserInfoRequestDto.getPlatformId(),
                updateUserInfoRequestDto.getPlatformType(),
                updateUserInfoRequestDto.getPhoneNumber(),
                updateUserInfoRequestDto.getIsPublic()
        );
    }

    @Override
    public void delete(Long userId) {
        // TODO : 삭제 권한은 MASTER에게만 존재한다.
        User user = userRepository.findByUserId(userId)
                .orElseThrow(()-> new UserNotFoundException(USER_NOT_FOUND_MESSAGE));
        user.deleteUser(userId);
    }

    private boolean newPasswordMatching(UpdatePasswordRequestDto updatePasswordRequestDto) {
        return updatePasswordRequestDto.getNewPassword()
                .matches(updatePasswordRequestDto.getConfirmPassword());
    }
}
