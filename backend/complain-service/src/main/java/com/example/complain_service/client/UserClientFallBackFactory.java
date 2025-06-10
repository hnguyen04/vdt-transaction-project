package com.example.complain_service.client;

import com.example.complain_service.common.constants.RoleEnum;
import com.example.complain_service.common.dto.response.ApiResponse;
import com.example.complain_service.common.dto.response.BaseGetAllResponse;
import com.example.complain_service.dto.user.UserResponse;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.UUID;

@Component
public class UserClientFallBackFactory implements FallbackFactory<UserClient> {

    @Override
    public UserClient create(Throwable cause) {
        System.err.println("UserClient fallback triggered due to: " + cause.getMessage());
        return new UserClient() {
            @Override
            public ApiResponse<BaseGetAllResponse<UserResponse>> getAllUsers(
                    int skipCount,
                    int maxResultCount,
                    String keyword,
                    RoleEnum role,
                    String userName,
                    String fullName,
                    String phoneNumber,
                    String cmnd,
                    String code
            ) {
                BaseGetAllResponse<UserResponse> emptyResponse = BaseGetAllResponse.<UserResponse>builder()
                        .data(Collections.emptyList())
                        .totalRecords(0)
                        .build();

                return ApiResponse.<BaseGetAllResponse<UserResponse>>builder()
                        .success(false)
                        .code(503)
                        .message("User service currently unavailable. Please try again later.")
                        .result(emptyResponse)
                        .build();
            }

            @Override
            public ApiResponse<UserResponse> getUserById(UUID id) {
                return ApiResponse.<UserResponse>builder()
                        .success(false)
                        .code(503)
                        .message("User service currently unavailable. Please try again later.")
                        .build();
            }
        };
    }
}
