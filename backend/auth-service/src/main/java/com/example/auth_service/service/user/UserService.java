package com.example.auth_service.service.user;

import com.example.auth_service.common.constants.RoleEnum;
import com.example.auth_service.common.dto.response.BaseGetAllResponse;
import com.example.auth_service.common.exception.AppException;
import com.example.auth_service.common.exception.ErrorCode;
import com.example.auth_service.dto.auth.RegisterRequest;
import com.example.auth_service.dto.user.UpdateUserRequest;
import com.example.auth_service.dto.user.UserGetAllRequest;
import com.example.auth_service.dto.user.UserResponse;
import com.example.auth_service.entity.User;
import com.example.auth_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    @Override
    public UserResponse createUser(RegisterRequest request) {
        if (userRepository.existsByUserName(request.getUserName())) {
            throw new AppException(ErrorCode.BAD_REQUEST, "Username already exists");
        }
        if (userRepository.existsByCmnd(request.getCmnd())) {
            throw new AppException(ErrorCode.BAD_REQUEST, "Cmnd already exists");
        }
        if (userRepository.existsByPhoneNumber(request.getPhoneNumber())) {
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
        return toUserResponse(user);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    @Override
    public UserResponse updateUser(UpdateUserRequest request) {
        UUID userId = request.getId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND, "User not found"));

        if (!user.getCmnd().equals(request.getCmnd()) && userRepository.existsByCmnd(request.getCmnd())) {
            throw new AppException(ErrorCode.BAD_REQUEST, "Cmnd already exists");
        }
        if (!user.getPhoneNumber().equals(request.getPhoneNumber()) && userRepository.existsByPhoneNumber(request.getPhoneNumber())) {
            throw new AppException(ErrorCode.BAD_REQUEST, "Phone number already exists");
        }

        user.setFullName(request.getFullName());
        user.setCmnd(request.getCmnd());
        user.setPhoneNumber(request.getPhoneNumber());

        userRepository.save(user);
        return toUserResponse(user);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    @Override
    public void deleteUser(UUID id) {
        userRepository.softDeleteById(id);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    @Override
    public User getUserEntityById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND, "User not found"));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    @Override
    public UserResponse getUserById(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND, "User not found"));
        return toUserResponse(user);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    @Override
    public BaseGetAllResponse<UserResponse> getAllUsers(UserGetAllRequest request) {
        int skipCount = Optional.ofNullable(request.getSkipCount()).orElse(0);
        int maxResultCount = Optional.ofNullable(request.getMaxResultCount()).orElse(10);

        String userName = normalize(request.getUserName());
        String fullName = normalize(request.getFullName());
        String phoneNumber = normalize(request.getPhoneNumber());
        String cmnd = normalize(request.getCmnd());
        String code = normalize(request.getCode());
        RoleEnum role = request.getRole();
        String keyword = normalize(request.getKeyword());

        List<User> users = userRepository.findAllByFilters(userName, fullName, phoneNumber, cmnd, role, keyword, code);

        List<UserResponse> result = users.stream()
                .skip(skipCount)
                .limit(maxResultCount)
                .map(this::toUserResponse)
                .collect(Collectors.toList());

        return BaseGetAllResponse.<UserResponse>builder()
                .data(result)
                .totalRecords(users.size())
                .build();
    }

    private String normalize(String input) {
        return (input == null || input.isBlank()) ? null : input.trim();
    }

    public UserResponse toUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .userName(user.getUserName())
                .fullName(user.getFullName())
                .code(user.getCode())
                .phoneNumber(user.getPhoneNumber())
                .cmnd(user.getCmnd())
                .role(user.getRole())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

    private String generateUserCode() {
        return UUID.randomUUID().toString().substring(0, 8);
    }
}
