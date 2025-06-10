package com.example.complain_service.client;


import com.example.complain_service.common.constants.RoleEnum;
import com.example.complain_service.common.dto.response.ApiResponse;
import com.example.complain_service.common.dto.response.BaseGetAllResponse;
import com.example.complain_service.config.FeignClientConfiguration;
import com.example.complain_service.dto.user.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@FeignClient(
        name = "user-service",
        url = "${user.service.url}",
        fallbackFactory = UserClientFallBackFactory.class,
        configuration = FeignClientConfiguration.class

)
public interface UserClient {

    @GetMapping("/users/GetAll")
    ApiResponse<BaseGetAllResponse<UserResponse>> getAllUsers(
            @RequestParam(value = "skipCount", defaultValue = "0") int skipCount,
            @RequestParam(value = "maxResultCount", defaultValue = "10") int maxResultCount,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "role", required = false) RoleEnum role,
            @RequestParam(value = "userName", required = false) String userName,
            @RequestParam(value = "fullName", required = false) String fullName,
            @RequestParam(value = "phoneNumber", required = false) String phoneNumber,
            @RequestParam(value = "cmnd", required = false) String cmnd,
            @RequestParam(value = "code", required = false) String code
    );

    @GetMapping("/users/GetById")
    ApiResponse<UserResponse> getUserById(@RequestParam("id") UUID id);
}