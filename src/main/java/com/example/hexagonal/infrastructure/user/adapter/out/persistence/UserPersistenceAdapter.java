package com.example.hexagonal.infrastructure.user.adapter.out.persistence;

import com.example.hexagonal.application.user.port.out.LoadUserPort;
import com.example.hexagonal.application.user.port.out.SaveUserPort;
import com.example.hexagonal.application.user.port.out.UpdateUserLoginPort;
import com.example.hexagonal.domain.user.User;
import com.example.hexagonal.domain.user.exception.UserNotFoundException;
import com.example.hexagonal.domain.user.vo.Email;
import com.example.hexagonal.domain.user.vo.UserId;
import com.example.hexagonal.infrastructure.user.entity.UserEntity;
import com.example.hexagonal.infrastructure.user.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserPersistenceAdapter implements LoadUserPort, SaveUserPort, UpdateUserLoginPort {

    private final UserJpaRepository userJpaRepository;
    private final UserMapper userMapper;

    @Override
    public Optional<User> loadById(UserId userId) {
        return userJpaRepository.findByPublicId(userId.value())
                .map(userMapper::toDomain);
    }

    @Override
    public Optional<User> loadByEmail(Email email) {
        return userJpaRepository.findByEmail(email.value())
                .map(userMapper::toDomain);
    }

    @Override
    public User save(User user) {
        Optional<UserEntity> existingOptional =
                userJpaRepository.findByPublicId(user.getId().value());

        if (existingOptional.isEmpty()) {
            UserEntity newEntity = userMapper.toEntity(user);
            UserEntity saved = userJpaRepository.save(newEntity);
            return userMapper.toDomain(saved);
        }

        UserEntity existing = existingOptional.get();
        userMapper.applyFullUpdate(user, existing);

        UserEntity updated = userJpaRepository.save(existing);
        return userMapper.toDomain(updated);
    }

    @Override
    public void updateLastLoginAt(UserId userId) {
        UserEntity entity = userJpaRepository.findByPublicId(userId.value())
                .orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다: " + userId.value()));

        entity.updateLastLoginAt();
    }

}