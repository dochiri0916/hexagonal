package com.example.hexagonal.application.user.port.in;

import com.example.hexagonal.application.user.dto.LoginUserCommand;
import com.example.hexagonal.application.user.dto.LoginUserResult;

public interface LoginUserUseCase {

    LoginUserResult login(LoginUserCommand loginUserCommand);

}