package com.example.hexagonal.presentation.auth.response;

import com.example.hexagonal.application.user.dto.LoginUserResult;

public record LoginUserResponse(
        String id,
        String email,
        String role,
        String accessToken
) {
    public static LoginUserResponse from(LoginUserResult result) {
        return new LoginUserResponse(
                result.id(),
                result.email(),
                result.role(),
                result.accessToken()
        );
    }
}