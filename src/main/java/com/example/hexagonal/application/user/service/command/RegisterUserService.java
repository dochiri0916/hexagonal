package com.example.hexagonal.application.user.service.command;

import com.example.hexagonal.application.user.dto.RegisterUserCommand;
import com.example.hexagonal.application.user.dto.RegisterUserResult;
import com.example.hexagonal.application.user.port.in.RegisterUserUseCase;
import com.example.hexagonal.application.user.port.out.LoadUserPort;
import com.example.hexagonal.application.user.port.out.SaveUserPort;
import com.example.hexagonal.domain.user.User;
import com.example.hexagonal.domain.user.exception.DuplicateEmailException;
import com.example.hexagonal.domain.user.vo.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RegisterUserService implements RegisterUserUseCase {

    private final LoadUserPort loadUserPort;
    private final SaveUserPort saveUserPort;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public RegisterUserResult register(RegisterUserCommand registerUserCommand) {
        Email email = Email.from(registerUserCommand.email());

        loadUserPort.loadByEmail(email)
                .ifPresent(existing -> {
                    throw new DuplicateEmailException(email.value());
                });

        String passwordHash = passwordEncoder.encode(registerUserCommand.password());

        User newUser = User.register(email, passwordHash, registerUserCommand.name());

        User savedUser = saveUserPort.save(newUser);

        return RegisterUserResult.of(
                savedUser.getId().value(),
                savedUser.getEmail().value()
        );
    }

}