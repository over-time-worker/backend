package com.owlexpress.user.application.service;

import com.owlexpress.user.presentation.dto.request.ApprovalUserRequestDto;
import com.owlexpress.user.presentation.dto.request.UpdateUserInfoRequestDto;
import com.owlexpress.user.presentation.dto.request.UpdateUserRoleRequestDto;
import com.owlexpress.user.presentation.dto.response.GetUsersResponseDto;
import org.springframework.data.web.PagedModel;

public interface MasterService {
    void updateRole(UpdateUserRoleRequestDto updateUserRoleRequestDto);

    void updateUserInfo (UpdateUserInfoRequestDto updateUserInfoRequestDto);

    void delete(String passport);

    void approvalUser(ApprovalUserRequestDto approvalUserRequestDto);

    PagedModel<GetUsersResponseDto> searchUsers(
            String keyword,
            int page,
            int size,
            String orderBy,
            String sort
    );
}
