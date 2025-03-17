package com.owlexpress.user.domain.repository;

import com.owlexpress.user.domain.entity.User;
import java.util.Optional;

public interface UserRepository {
    void save(User user);

    Optional<User> findByAccountId(String accountId);

    Optional<User> findByUserId(Long userId);

    Optional<User> updatePassword(Long userId);

    Optional<User> updateInfo(Long userId);

    Optional<User> delete(Long userId);
}
