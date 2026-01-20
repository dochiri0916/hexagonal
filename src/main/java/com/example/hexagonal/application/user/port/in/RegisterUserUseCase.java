package com.example.hexagonal.application.user.port.in;

import com.example.hexagonal.application.user.dto.RegisterUserCommand;
import com.example.hexagonal.application.user.dto.RegisterUserResult;

public interface RegisterUserUseCase {

    RegisterUserResult register(RegisterUserCommand registerUserCommand);

}