package com.example.hexagonal.application.user.service.command;

import com.example.hexagonal.application.user.dto.LoginUserCommand;
import com.example.hexagonal.application.user.dto.LoginUserResult;
import com.example.hexagonal.application.user.port.in.LoginUserUseCase;
import com.example.hexagonal.application.user.port.out.JwtTokenPort;
import com.example.hexagonal.application.user.port.out.UpdateUserLoginPort;
import com.example.hexagonal.application.user.service.query.UserFinder;
import com.example.hexagonal.domain.user.User;
import com.example.hexagonal.domain.user.UserStatus;
import com.example.hexagonal.domain.user.exception.InactiveUserException;
import com.example.hexagonal.domain.user.exception.InvalidPasswordException;
import com.example.hexagonal.domain.user.vo.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LoginUserService implements LoginUserUseCase {

    private final UserFinder userFinder;
    private final UpdateUserLoginPort updateUserLoginPort;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenPort jwtTokenPort;

    @Transactional
    @Override
    public LoginUserResult login(LoginUserCommand loginUserCommand) {
        Email email = Email.from(loginUserCommand.email());
        User user = userFinder.findByEmail(email);

        if (!passwordEncoder.matches(loginUserCommand.password(), user.getPasswordHash())) {
            throw new InvalidPasswordException("비밀번호가 일치하지 않습니다.");
        }

        if (user.getStatus() != UserStatus.ACTIVE) {
            throw new InactiveUserException("활성화되지 않은 계정입니다.");
        }

        user.updateLastLoginAt();

        updateUserLoginPort.updateLastLoginAt(user.getId());

        String accessToken = jwtTokenPort.generateAccessToken(
                user.getId().value(),
                user.getRole().name()
        );

        return LoginUserResult.of(
                user.getId().value(),
                user.getEmail().value(),
                user.getRole().name(),
                accessToken
        );
    }

}