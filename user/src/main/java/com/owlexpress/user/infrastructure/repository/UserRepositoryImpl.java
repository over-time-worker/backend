package com.owlexpress.user.infrastructure.repository;

import static com.owlexpress.user.common.exception.ExceptionMessage.EXIST_ACCOUNT_MESSAGE;

import com.owlexpress.user.domain.entity.User;
import com.owlexpress.user.domain.repository.UserRepository;
import com.owlexpress.user.presentation.dto.response.GetUsersResponseDto;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public Optional<User> findByAccountId(String accountId) {
        return userJpaRepository.findByAccountId(accountId);
    }

    @Override
    public Optional<User> findByUserId(Long userId) {
        return userJpaRepository.findById(userId);
    }

    @Override
    public Optional<User> updatePassword(Long userId) {
        return userJpaRepository.findById(userId);
    }

    @Override
    public Optional<User> updateInfo(Long userId) {
        return userJpaRepository.findById(userId);
    }

    @Override
    public Optional<User> delete(Long userId) {
        return userJpaRepository.findById(userId);
    }

    @Override
    public Page<GetUsersResponseDto> searchUsers(String keyword, Pageable pageable) {
        return userQueryDSLRepository.searchUsers(pageable, keyword);
    }

    public boolean existsByAccountId(String accountId) {
        return userJpaRepository.existsByAccountId(accountId);
    }
}
