package com.example.hexagonal.application.user.dto;

public record LoginUserCommand(
        String email,
        String password
) {
}