package com.owlexpress.user.infrastructure.repository;

import static com.owlexpress.user.common.exception.ExceptionMessage.EXIST_ACCOUNT_MESSAGE;
import static com.owlexpress.user.common.exception.ExceptionMessage.USER_NOT_FOUND_MESSAGE;

import com.owlexpress.user.domain.entity.User;
import com.owlexpress.user.domain.repository.UserRepository;
import com.owlexpress.user.infrastructure.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
    private final UserJpaRepository userJpaRepository;
    private final UserQueryDSLRepository userQueryDSLRepository;

    @Override
    public void save(User user) {
        if (existsByAccountId(user.getAccountId())) {
            throw new IllegalArgumentException(EXIST_ACCOUNT_MESSAGE);
        }
        userJpaRepository.save(user);
    }

    @Override
    public User findByAccountId(String accountId) {
        return userJpaRepository.findByAccountId(accountId)
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND_MESSAGE));
    }

    @Override
    public User findByUserId(Long userId) {
        return userJpaRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND_MESSAGE));
    }

    @Override
    public User updatePassword(Long userId) {
        return userJpaRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND_MESSAGE));
    }

    @Override
    public User updateInfo(Long userId) {
        return userJpaRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND_MESSAGE));
    }

    @Override
    public User delete(Long userId) {
        return userJpaRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND_MESSAGE));
    }

    public boolean existsByAccountId(String accountId) {
        return userJpaRepository.existsByAccountId(accountId);
    }
}
