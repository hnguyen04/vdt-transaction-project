package com.example.auth_service.service.user;

import com.example.auth_service.common.constants.CacheKeys;
import com.example.auth_service.common.dto.response.BaseGetAllResponse;
import com.example.auth_service.dto.auth.RegisterRequest;
import com.example.auth_service.dto.user.*;
import com.example.auth_service.entity.User;
import com.example.auth_service.service.RedisService;
import lombok.RequiredArgsConstructor;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
public class UserServiceCacheDecorator implements IUserService {

    private final IUserService userService;
    private final RedisService redisService;

    private static final String USER_CACHE_PREFIX = CacheKeys.USER_PREFIX;

    @Override
    public UserResponse createUser(RegisterRequest request) {

        UserResponse response = userService.createUser(request);
        redisService.set(USER_CACHE_PREFIX + response.getId(), response, 10, TimeUnit.MINUTES);
        return response;
    }

    @Override
    public UserResponse updateUser(UpdateUserRequest request) {
        UserResponse response = userService.updateUser(request);
        redisService.delete(USER_CACHE_PREFIX + request.getId());
        redisService.set(USER_CACHE_PREFIX + response.getId(), response, 10, TimeUnit.MINUTES);
        return response;
    }

    @Override
    public void deleteUser(UUID id) {
        userService.deleteUser(id);
        redisService.delete(USER_CACHE_PREFIX + id);
    }

    @Override
    public UserResponse getUserById(UUID id) {
        String key = USER_CACHE_PREFIX + id;
        UserResponse cachedUserResponse = redisService.get(key, UserResponse.class);
        if (cachedUserResponse != null) {
            return cachedUserResponse;
        }
        UserResponse userResponse = userService.getUserById(id);
        redisService.set(key, userResponse, 10, TimeUnit.MINUTES);
        return userResponse;
    }

    @Override
    public BaseGetAllResponse<UserResponse> getAllUsers(UserGetAllRequest request) {
        return userService.getAllUsers(request);
    }

    @Override
    public User getUserEntityById(UUID id) {
        // Chỉ gọi decorate với getUserById, không dùng cache cho getUserEntityById
        return userService.getUserEntityById(id);
    }

    @Override
    public void changePassword(ChangePasswordRequest request) {
        userService.changePassword(request);
        // Không cần cache cho thay đổi mật khẩu
    }

    @Override
    public void resetPassword(ResetPasswordRequest request) {
        userService.resetPassword(request);
        // Không cần cache cho reset mật khẩu
    }

    @Override
    public UserResponse getMyInfo() {
        UserResponse userResponse = userService.getMyInfo();
        String key = USER_CACHE_PREFIX + userResponse.getId();
        redisService.set(key, userResponse, 10, TimeUnit.MINUTES);
        return userResponse;
    }

    @Override
    public UserConfigurationResponse getUserConfiguration() {
        return userService.getUserConfiguration();
    }

    public UserResponse toUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .userName(user.getUserName())
                .fullName(user.getFullName())
                .code(user.getCode())
                .phoneNumber(user.getPhoneNumber())
                .cmnd(user.getCmnd())
                .role(user.getRole())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

}
