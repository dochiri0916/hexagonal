package com.example.hexagonal.application.user.port.out;

import com.example.hexagonal.domain.user.User;

public interface SaveUserPort {

    User save(User user);

}