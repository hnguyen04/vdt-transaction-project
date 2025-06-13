package com.example.auth_service.controller;

import com.example.auth_service.common.dto.response.ApiResponse;
import com.example.auth_service.dto.auth.*;
import com.example.auth_service.service.auth.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class AuthController {

    AuthService authService;

    @PostMapping("/register")
    public ApiResponse<RegisterResponse> register(@RequestBody RegisterRequest request) {
        var result = authService.register(request);
        return ApiResponse.<RegisterResponse>builder()
                .result(result)
                .build();
    }

    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@RequestBody LoginRequest request) {
        var result = authService.login(request);
        return ApiResponse.<LoginResponse>builder()
                .result(result)
                .build();
    }

    @PostMapping("/logout")
    public ApiResponse<Void> logout(LogoutRequest request) {

        authService.logout(request.getToken());
        return ApiResponse.<Void>builder().build();
    }

    @PostMapping("/refresh")
    public ApiResponse<RefreshTokenResponse> refresh(@RequestBody RefreshTokenRequest request) {
        var result = authService.refreshToken(request);
        return ApiResponse.<RefreshTokenResponse>builder()
                .result(result)
                .build();
    }

    @PostMapping("/introspect")
    public ApiResponse<IntrospectResponse> introspect(@RequestBody IntrospectRequest request) {

        var result = authService.introspect(request.getToken());
        return ApiResponse.<IntrospectResponse>builder()
                .result(result)
                .code(result.getUserName() != null ? 200 : 401)
                .build();
    }

    @GetMapping("/whoami")
    public ResponseEntity<?> whoami(Authentication authentication) {
        System.out.println("authenticatedwith" + authentication);
        return ResponseEntity.ok(authentication.getAuthorities());

    }


}

