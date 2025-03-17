package com.owlexpress.user.infrastructure.repository;

import com.owlexpress.user.presentation.dto.response.GetUsersResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserQueryDSLRepository {
    Page<GetUsersResponseDto> searchUsers(Pageable pageable, String keyword);
}
