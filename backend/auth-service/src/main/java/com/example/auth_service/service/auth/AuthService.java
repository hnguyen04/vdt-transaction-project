package com.example.auth_service.service.auth;

import com.example.auth_service.common.exception.AppException;
import com.example.auth_service.common.exception.ErrorCode;
import com.example.auth_service.config.JwtUtil;
import com.example.auth_service.dto.auth.*;
import com.example.auth_service.entity.User;
import com.example.auth_service.repository.UserRepository;
import com.example.auth_service.service.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtUtil jwtUtil;
    private final RedisService redisService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public RegisterResponse register(RegisterRequest request) {
        if (userRepository.existsByUserName(request.getUserName())) {
            throw new AppException(ErrorCode.BAD_REQUEST, "Username already exists");
        }
        if (userRepository.existsByCmnd(request.getCmnd())) {
            throw new AppException(ErrorCode.BAD_REQUEST, "Cmnd already exists");
        }
        if (userRepository.existsByPhoneNumber(request.getCmnd())) {
            throw new AppException(ErrorCode.BAD_REQUEST, "Phone number already exists");
        }


        User user = User.builder()
                .userName(request.getUserName())
                .fullName(request.getFullName())
                .code(generateUserCode())
                .phoneNumber(request.getPhoneNumber())
                .cmnd(request.getCmnd())
                .role(request.getRole())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        userRepository.save(user);

        return new RegisterResponse(user.getId().toString(), "User registered successfully");
    }

    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByUserName(request.getUserName())
                .orElseThrow(() -> new AppException(ErrorCode.BAD_REQUEST, "Invalid username or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new AppException(ErrorCode.BAD_REQUEST, "Invalid username or password");
        }

        String accessToken = jwtUtil.generateToken(user.getId().toString(), user.getRole().name());
        String refreshToken = jwtUtil.generateRefreshToken(user.getId().toString());

        return new LoginResponse(accessToken, refreshToken, jwtUtil.getExpirationMillis(accessToken));
    }

    public void logout(String token) {
        long ttl = jwtUtil.getExpirationMillis(token);
        if (ttl > 0) {
            // Lưu token vào Redis blacklist với TTL = thời gian còn lại của token
            redisService.set(token, "blacklisted", ttl, TimeUnit.MILLISECONDS);
        }
    }

    public RefreshTokenResponse refreshToken(RefreshTokenRequest request) {
        String refreshToken = request.getRefreshToken();

        if (!jwtUtil.validateToken(refreshToken)) {
            throw new AppException(ErrorCode.UNAUTHORIZED, "Invalid refresh token");
        }

        if (redisService.hasKey(refreshToken)) {
            throw new AppException(ErrorCode.UNAUTHORIZED, "Refresh token has been blacklisted");
        }

        String userId = jwtUtil.getUserIdFromToken(refreshToken);

        // Tạo token mới
        User user = userRepository.findById(UUID.fromString(userId)).
                   orElseThrow(() -> new AppException(ErrorCode.BAD_REQUEST, "User not found"));


        String newAccessToken = jwtUtil.generateToken(user.getId().toString(), user.getRole().name());
        String newRefreshToken = jwtUtil.generateRefreshToken(user.getId().toString());

        // Blacklist refresh token cũ
        long ttl = jwtUtil.getExpirationMillis(refreshToken);
        redisService.set(refreshToken, "blacklisted", ttl, TimeUnit.MILLISECONDS);

        return new RefreshTokenResponse(newAccessToken, newRefreshToken, jwtUtil.getExpirationMillis(newAccessToken));
    }

    public IntrospectResponse introspect(String token) {
        if (!jwtUtil.validateToken(token)) {
            return new IntrospectResponse(false, null, null, null, 0);
        }

        if (redisService.hasKey(token)) {
            return new IntrospectResponse(false, null, null, null, 0);
        }

        String userId = jwtUtil.getUserIdFromToken(token);
        String role = jwtUtil.getRoleFromToken(token);

        User user = userRepository.findById(UUID.fromString(userId)).
                orElseThrow(() -> new AppException(ErrorCode.BAD_REQUEST, "User not found"));

        long expiresIn = jwtUtil.getExpirationMillis(token);

        return new IntrospectResponse(true, user.getId(), user.getUserName(), role, expiresIn);
    }

    // Giả sử tự viết method tạo code 8 số:
    private String generateUserCode() {
        // Ví dụ tạo code 8 số ngẫu nhiên
        return String.format("%08d", (int)(Math.random() * 100_000_000));
    }
}
