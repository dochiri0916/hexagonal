package com.example.hexagonal.application.user.port.out;

import com.example.hexagonal.domain.user.User;
import com.example.hexagonal.domain.user.vo.Email;
import com.example.hexagonal.domain.user.vo.UserId;

import java.util.Optional;

public interface LoadUserPort {

    Optional<User> loadById(UserId userId);

    Optional<User> loadByEmail(Email email);

}