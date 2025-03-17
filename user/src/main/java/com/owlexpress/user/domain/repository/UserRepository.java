package com.owlexpress.user.domain.repository;

import com.owlexpress.user.domain.entity.User;

public interface UserRepository {
    void save(User user);

    User findByAccountId(String accountId);

    User findByUserId(Long userId);

    User updatePassword(Long userId);

    User updateInfo(Long userId);

    User delete(Long userId);
}
