package com.example.hexagonal.application.user.service.query;

import com.example.hexagonal.application.user.port.out.LoadUserPort;
import com.example.hexagonal.domain.user.User;
import com.example.hexagonal.domain.user.exception.UserNotFoundException;
import com.example.hexagonal.domain.user.vo.Email;
import com.example.hexagonal.domain.user.vo.UserId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserFinder {

    private final LoadUserPort loadUserPort;

    @Transactional(readOnly = true)
    public User findById(UserId userId) {
        return loadUserPort.loadById(userId)
                .orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다: " + userId.value()));
    }

    @Transactional(readOnly = true)
    public User findByEmail(Email email) {
        return loadUserPort.loadByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다: " + email.value()));

    }

}