package com.example.hexagonal.application.user.port.out;

import com.example.hexagonal.domain.user.vo.UserId;

public interface UpdateUserLoginPort {
    void updateLastLoginAt(UserId userId);
}