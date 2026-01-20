package com.example.hexagonal.application.user.port.in;

import com.example.hexagonal.application.user.dto.UserProfileResult;

public interface GetUserProfileQuery {

    UserProfileResult getProfile(String userPublicId);

}