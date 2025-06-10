package com.example.transaction_service.config;

import java.util.List;

public class PublicEndpoints {
    public static final List<String> ENDPOINTS = List.of(
            "/auth/login",
            "/auth/register",
            "/auth/refresh",
            "/public/**",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/swagger-ui.html"
    );
}
