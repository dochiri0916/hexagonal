package com.example.hexagonal.presentation.auth;

import com.example.hexagonal.application.user.dto.LoginUserCommand;
import com.example.hexagonal.application.user.dto.LoginUserResult;
import com.example.hexagonal.application.user.dto.RegisterUserCommand;
import com.example.hexagonal.application.user.dto.RegisterUserResult;
import com.example.hexagonal.application.user.port.in.GetUserProfileQuery;
import com.example.hexagonal.application.user.port.in.LoginUserUseCase;
import com.example.hexagonal.application.user.port.in.RegisterUserUseCase;
import com.example.hexagonal.presentation.auth.request.LoginUserRequest;
import com.example.hexagonal.presentation.auth.request.RegisterUserRequest;
import com.example.hexagonal.presentation.auth.response.LoginUserResponse;
import com.example.hexagonal.presentation.auth.response.RegisterUserResponse;
import com.example.hexagonal.presentation.auth.response.UserProfileResponse;
import com.example.hexagonal.presentation.common.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final RegisterUserUseCase registerUserUseCase;
    private final LoginUserUseCase loginUserUseCase;
    private final GetUserProfileQuery getUserProfileQuery;

    @PostMapping("/register")
    public ApiResponse<RegisterUserResponse> register(@Valid @RequestBody RegisterUserRequest registerUserRequest) {
        RegisterUserResult result = registerUserUseCase.register(
                new RegisterUserCommand(
                        registerUserRequest.email(),
                        registerUserRequest.password(),
                        registerUserRequest.name()
                )
        );
        return ApiResponse.success(RegisterUserResponse.from(result));
    }

    @PostMapping("/login")
    public ApiResponse<LoginUserResponse> login(@Valid @RequestBody LoginUserRequest loginUserRequest) {
        LoginUserResult result = loginUserUseCase.login(
                new LoginUserCommand(
                        loginUserRequest.email(),
                        loginUserRequest.password()
                )
        );
        return ApiResponse.success(LoginUserResponse.from(result));
    }

    @GetMapping("/me")
    public ApiResponse<UserProfileResponse> getProfile(@AuthenticationPrincipal String userPublicId) {
        return ApiResponse.success(UserProfileResponse.from(
                        getUserProfileQuery.getProfile(userPublicId)
                )
        );
    }

}