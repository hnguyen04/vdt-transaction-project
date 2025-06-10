package com.example.auth_service.dto.user;

import com.example.auth_service.common.constants.RoleEnum;
import com.example.auth_service.common.dto.request.BaseGetAllRequest;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserGetAllRequest extends BaseGetAllRequest {
    String userName;
    String fullName;
    String phoneNumber;
    String cmnd;
    RoleEnum role;
    String code;
}
