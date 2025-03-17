package com.owlexpress.user.domain.repository;

import com.owlexpress.user.domain.entity.User;
import com.owlexpress.user.presentation.dto.response.GetUsersResponseDto;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserRepository {
    void save(User user);

    Optional<User> findByAccountId(String accountId);

    Optional<User> findByUserId(Long userId);

    Page<GetUsersResponseDto> searchUsers(String keyword, Pageable pageable);
}
