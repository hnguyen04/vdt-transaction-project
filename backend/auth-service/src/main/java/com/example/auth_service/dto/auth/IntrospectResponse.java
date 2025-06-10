package com.example.auth_service.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IntrospectResponse {
    private boolean active;  // token còn hiệu lực?
    private UUID userId;
    private String userName;
    private String role;
    private long expiresIn;
}
