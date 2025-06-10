package com.example.auth_service.config;

import com.example.auth_service.common.exception.AppException;
import com.example.auth_service.common.exception.ErrorCode;
import com.example.auth_service.service.RedisService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filter để kiểm tra JWT trong mỗi request và gán thông tin người dùng vào SecurityContext
 */
@Component
@RequiredArgsConstructor
public class JwtBlackListFilter extends OncePerRequestFilter {

    private final RedisService redisService; // Dịch vụ để kiểm tra token có bị blacklist không
    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String token = parseJwt(request);

        if (token != null && jwtUtil.validateToken(token)) {
            if (redisService.hasKey(token)) {
                // Nếu token bị blacklist, trả lỗi 401 và dừng filter chain
                throw new AppException(ErrorCode.UNAUTHORIZED, "Token has been blacklisted");
            }
        }

        // Tiếp tục chuỗi filter bình thường nếu token không bị blacklist hoặc không có token
        filterChain.doFilter(request, response);
    }

    /**
     * Lấy JWT token từ header Authorization
     */
    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }
        return null;
    }
}
