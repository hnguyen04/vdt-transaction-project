package com.example.auth_service.service;

import com.example.auth_service.repository.UserRepository;
import com.example.auth_service.service.user.IUserService;
import com.example.auth_service.service.user.UserService;
import com.example.auth_service.service.user.UserServiceCacheDecorator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class ServiceConfig {

    @Bean
    public IUserService userService(UserRepository userRepository,
                                    PasswordEncoder passwordEncoder,
                                    RedisService redisService) {
        IUserService impl = new UserService(userRepository, passwordEncoder);
        return new UserServiceCacheDecorator(impl, redisService);
    }
}