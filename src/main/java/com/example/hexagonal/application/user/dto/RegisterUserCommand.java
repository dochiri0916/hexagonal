package com.example.hexagonal.application.user.dto;

public record RegisterUserCommand(
        String email,
        String password,
        String name
) {
}