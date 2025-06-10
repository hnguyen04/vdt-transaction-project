package com.example.transaction_service.config;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Configuration
public class FeignClientConfiguration {

    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
            if (requestAttributes instanceof ServletRequestAttributes servletRequestAttributes) {
                String token = servletRequestAttributes.getRequest().getHeader("Authorization");
                System.out.println("[FeignClientConfiguration] Token from HTTP header: " + token);
                if (token != null) {
                    requestTemplate.header("Authorization", token);
                }
            } else {
                System.out.println("[FeignClientConfiguration] No ServletRequestAttributes available");
            }
        };
    }
}
