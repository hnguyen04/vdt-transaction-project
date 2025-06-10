package com.example.auth_service.dto.auth;

import com.example.auth_service.common.constants.RoleEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    private String userName;
    private String password;
    private String fullName;
    private String phoneNumber;
    private String cmnd;
    private RoleEnum role;
}