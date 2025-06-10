package com.example.auth_service.controller;


import com.example.auth_service.common.constants.RoleEnum;
import com.example.auth_service.common.dto.response.ApiResponse;
import com.example.auth_service.common.dto.response.BaseGetAllResponse;
import com.example.auth_service.dto.auth.RegisterRequest;
import com.example.auth_service.dto.user.UpdateUserRequest;
import com.example.auth_service.dto.user.UserGetAllRequest;
import com.example.auth_service.dto.user.UserResponse;
import com.example.auth_service.service.user.IUserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserController {

    IUserService userService;

    @PostMapping("/Create")
    ApiResponse<UserResponse> createUser(@RequestBody RegisterRequest request) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.createUser(request))
                .build();
    }

    @PutMapping("/Update")
    ApiResponse<UserResponse> updateUser(@RequestBody UpdateUserRequest request) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.updateUser(request))
                .build();
    }

    @GetMapping("/GetAll")
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
    ) {
        UserGetAllRequest request = UserGetAllRequest.builder()
                .userName(userName)
                .code(code)
                .cmnd(cmnd)
                .fullName(fullName)
                .phoneNumber(phoneNumber)
                .role(role)
                .build();
        request.setSkipCount(skipCount);
        request.setMaxResultCount(maxResultCount);
        request.setKeyword(keyword);

        return ApiResponse.<BaseGetAllResponse<UserResponse>>builder()
                .result(userService.getAllUsers(request))
                .build();
    }

    @GetMapping("/GetById")
    ApiResponse<UserResponse> getUserById(@RequestParam UUID id) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.getUserById(id))
                .build();
    }

    @DeleteMapping("/Delete")
    ApiResponse<String> deleteUser(@RequestParam UUID id) {
        userService.deleteUser(id);
        return ApiResponse.<String>builder()
                .result("User deleted successfully")
                .build();
    }

}
