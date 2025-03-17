package com.owlexpress.user.application.service;

import static com.owlexpress.user.common.exception.ExceptionMessage.USER_NOT_FOUND_MESSAGE;

import com.owlexpress.user.common.util.PageUtil;
import com.owlexpress.user.domain.entity.User;
import com.owlexpress.user.domain.repository.UserRepository;
import com.owlexpress.user.infrastructure.exception.UserNotFoundException;
import com.owlexpress.user.presentation.dto.request.ApprovalUserRequestDto;
import com.owlexpress.user.presentation.dto.request.UpdateUserInfoRequestDto;
import com.owlexpress.user.presentation.dto.request.UpdateUserRoleRequestDto;
import com.owlexpress.user.presentation.dto.response.GetUsersResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MasterServiceImpl implements MasterService {
    private final UserRepository userRepository;

    @Override
    public void updateRole(UpdateUserRoleRequestDto updateUserRoleRequestDto) {
        User user = userRepository.findByUserId(updateUserRoleRequestDto.getUserId())
                .orElseThrow(()-> new UserNotFoundException(USER_NOT_FOUND_MESSAGE));

        user.setRole(updateUserRoleRequestDto.getRole());
    }

    @Override
    public void updateUserInfo(UpdateUserInfoRequestDto updateUserInfoRequestDto) {
        User user = userRepository.findByUserId(updateUserInfoRequestDto.getUserId())
                .orElseThrow(()-> new UserNotFoundException(USER_NOT_FOUND_MESSAGE));

        user.setUserInfo(
                updateUserInfoRequestDto.getUserId(),
                updateUserInfoRequestDto.getPlatformId(),
                updateUserInfoRequestDto.getPlatformType(),
                updateUserInfoRequestDto.getPhoneNumber(),
                updateUserInfoRequestDto.getIsPublic()
        );
    }

    @Override
    public void delete(Long userId) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(()-> new UserNotFoundException(USER_NOT_FOUND_MESSAGE));
        user.deleteUser(userId);
    }

    @Override
    public void approvalUser(ApprovalUserRequestDto approvalUserRequestDto) {
        User user = userRepository.findByUserId(approvalUserRequestDto.getUserId())
                .orElseThrow(()-> new UserNotFoundException(USER_NOT_FOUND_MESSAGE));

        user.setApprovalStatus(approvalUserRequestDto.getApprovalStatus());
    }

    @Override
    public PagedModel<GetUsersResponseDto> searchUsers(
            String keyword,
            int page,
            int size,
            String orderBy,
            String sort
    ) {
        Pageable pageable = PageUtil.getPageable(page, size, sort, orderBy);
        Page<GetUsersResponseDto> paged = userRepository.searchUsers(keyword, pageable);
        return new PagedModel<>(paged);
    }
}
