package com.example.hexagonal.presentation.auth.response;

import com.example.hexagonal.application.user.dto.RegisterUserResult;

public record RegisterUserResponse(
        String id,
        String email
) {
    public static RegisterUserResponse from(RegisterUserResult result) {
        return new RegisterUserResponse(result.id(), result.email());
    }
}