package com.owlexpress.user.application.service;

import static com.owlexpress.user.common.exception.ExceptionMessage.USER_NOT_FOUND_MESSAGE;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.owlexpress.user.common.PassportHelper;
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
        User user = getUser(updateUserRoleRequestDto.getUserId());

        user.setRole(updateUserRoleRequestDto.getRole());
    }

    @Override
    public void updateUserInfo(UpdateUserInfoRequestDto updateUserInfoRequestDto) {
        User user = getUser(updateUserInfoRequestDto.getUserId());

        user.setUserInfo(
                updateUserInfoRequestDto.getUserId(),
                updateUserInfoRequestDto.getPlatformId(),
                updateUserInfoRequestDto.getPlatformType(),
                updateUserInfoRequestDto.getPhoneNumber(),
                updateUserInfoRequestDto.getIsPublic()
        );
    }

    @Override
    public void delete(String passport) {
        ObjectMapper objectMapper = new ObjectMapper();
        PassportHelper helper = objectMapper.convertValue(passport, PassportHelper.class);
        User user = getUser(helper.getUserId());
        user.deleteUser(helper.getUserId());
    }

    @Override
    public void approvalUser(ApprovalUserRequestDto approvalUserRequestDto) {
        User user = getUser(approvalUserRequestDto.getUserId());

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

    private User getUser(Long userId) {
        return userRepository.findByUserId(userId)
                .orElseThrow(()-> new UserNotFoundException(USER_NOT_FOUND_MESSAGE));
    }
}
