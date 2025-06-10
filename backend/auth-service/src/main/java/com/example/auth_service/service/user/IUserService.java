package com.example.auth_service.service.user;


import com.example.auth_service.common.dto.response.BaseGetAllResponse;
import com.example.auth_service.dto.auth.RegisterRequest;
import com.example.auth_service.dto.user.*;
import com.example.auth_service.entity.User;

import java.util.UUID;

public interface IUserService {

    UserResponse createUser(RegisterRequest request);

    UserResponse updateUser(UpdateUserRequest request);

    void deleteUser(UUID id);

    UserResponse getUserById(UUID id);

    BaseGetAllResponse<UserResponse> getAllUsers(UserGetAllRequest request);

    User getUserEntityById(UUID id);

    UserResponse toUserResponse(User user);

    void changePassword(ChangePasswordRequest request);

    void resetPassword(ResetPasswordRequest request);

    UserResponse getMyInfo();

    UserConfigurationResponse getUserConfiguration();
}
