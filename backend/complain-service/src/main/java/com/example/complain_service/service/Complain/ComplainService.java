package com.example.complain_service.service.Complain;

import com.example.complain_service.client.UserClient;
import com.example.complain_service.common.constants.CacheKeys;
import com.example.complain_service.common.constants.ComplainStatusEnum;
import com.example.complain_service.common.constants.RoleEnum;
import com.example.complain_service.common.dto.response.ApiResponse;
import com.example.complain_service.common.dto.response.BaseGetAllResponse;
import com.example.complain_service.common.exception.AppException;
import com.example.complain_service.common.exception.ErrorCode;
import com.example.complain_service.dto.complain.*;
import com.example.complain_service.dto.user.UserResponse;
import com.example.complain_service.entity.Complain;
import com.example.complain_service.repository.ComplainRepository;
import com.example.complain_service.service.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ComplainService {

    private final ComplainRepository repository;
    private final UserClient userClient;
    private final RedisService redisService;


    public ComplainResponse create(ComplainCreateRequest request) {
        var entity = com.example.complain_service.entity.Complain.builder()
                .userId(request.getUserId())
                .content(request.getContent())
                .status(ComplainStatusEnum.PENDING)
                .timeSubmit(LocalDateTime.now())
                .build();

        entity = repository.save(entity);

        // Lấy dữ liệu user/resolver
        UserResponse userData = fetchUserInfo(entity.getUserId());
        UserResponse resolverData = fetchUserInfo(entity.getResolverId());

        return mapToResponse(entity, userData, resolverData);
    }

    public ComplainResponse getById(UUID id) {
        var entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Complain not found with id: " + id));

        var userData = fetchUserInfo(entity.getUserId());
        var resolverData = fetchUserInfo(entity.getResolverId());

        return mapToResponse(entity, userData, resolverData);
    }

    public ComplainResponse update(ComplainUpdateRequest request) {
        var entity = repository.findById(request.getId())
                .orElseThrow(() -> new AppException(ErrorCode.BAD_REQUEST, "Complain not found with id: " + request.getId()));

        if (request.getContent() != null) {
            entity.setContent(request.getContent());
        }
        entity = repository.save(entity);

        var userData = fetchUserInfo(entity.getUserId());
        var resolverData = fetchUserInfo(entity.getResolverId());

        return mapToResponse(entity, userData, resolverData);
    }

    public void delete(UUID id) {
        var entity = repository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.BAD_REQUEST, "Complain not found with id: " + id));
    }

    public ComplainResponse claim(ComplainAssignRequest request) {
        var entity = repository.findById(request.getId())
                .orElseThrow(() -> new AppException(ErrorCode.BAD_REQUEST, "Complain not found with id: " + request.getId()));

        entity.setResolverId(request.getResolverId());
        entity = repository.save(entity);

        var user = fetchUserInfo(entity.getUserId());
        var resolver = fetchUserInfo(entity.getResolverId());

        return mapToResponse(entity, user, resolver);
    }

    public ComplainResponse assign(ComplainAssignRequest request) {
        var entity = repository.findById(request.getId())
                .orElseThrow(() -> new AppException(ErrorCode.BAD_REQUEST, "Complain not found with id: " + request.getId()));

        entity.setResolverId(request.getResolverId());
        entity = repository.save(entity);

        var user = fetchUserInfo(entity.getUserId());
        var resolver = fetchUserInfo(entity.getResolverId());

        return mapToResponse(entity, user, resolver);
    }

    public ComplainResponse resolve(ComplainAssignRequest request) {
        var entity = repository.findById(request.getId())
                .orElseThrow(() -> new AppException(ErrorCode.BAD_REQUEST, "Complain not found with id: " + request.getId()));


        entity.setResolverId(request.getResolverId());
        entity.setStatus(ComplainStatusEnum.RESOLVED);
        entity = repository.save(entity);

        var user = fetchUserInfo(entity.getUserId());
        var resolver = fetchUserInfo(entity.getResolverId());

        return mapToResponse(entity, user, resolver);
    }

    public ComplainResponse note(ComplainNoteRequest request) {
        var entity = repository.findById(request.getId())
                .orElseThrow(() -> new AppException(ErrorCode.BAD_REQUEST, "Complain not found with id: " + request.getId()));

        entity.setResolvingNote(request.getNote());
        entity = repository.save(entity);

        var user = fetchUserInfo(entity.getUserId());
        var resolver = fetchUserInfo(entity.getResolverId());

        return mapToResponse(entity, user, resolver);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN', 'STAFF')")
    public BaseGetAllResponse<ComplainResponse> getAll(ComplainGetAllRequest request) {
        int skipCount = Optional.ofNullable(request.getSkipCount()).orElse(0);
        int maxResultCount = Optional.ofNullable(request.getMaxResultCount()).orElse(10);

        String keyword = normalize(request.getKeyword());
        String userFullName = normalize(request.getUserFullName());
        String resolverFullName = normalize(request.getResolverFullName());
        String status = normalize(request.getStatus());
        UUID userId = request.getUserId() != null ? request.getUserId() : null;
        UUID resolverId = request.getResolverId() != null ? request.getResolverId() : null;

        List<ComplainResponse> result = new ArrayList<>();


        List<UUID> userIds = new ArrayList<>();
        if (userFullName != null || keyword != null) {
            ApiResponse<BaseGetAllResponse<UserResponse>> userResp = userClient.getAllUsers(
                    0, 1000, keyword, RoleEnum.USER,
                    null, userFullName,
                    null, null, null);

            userIds = userResp.getResult().getData().stream()
                    .map(UserResponse::getId)
                    .collect(Collectors.toList());
        } else if (request.getUserId() != null) {
            userIds.add(userId);
        }


        // Lấy resolverId list từ resolverFullName
        List<UUID> resolverIds = new ArrayList<>();
        if (resolverFullName != null || keyword != null) {
            ApiResponse<BaseGetAllResponse<UserResponse>> resolverResp = userClient.getAllUsers(
                    0, 1000, keyword, RoleEnum.STAFF,
                    null, resolverFullName,
                    null, null, null);
            resolverIds = resolverResp.getResult().getData().stream()
                    .map(UserResponse::getId)
                    .collect(Collectors.toList());
        } else if (request.getResolverId() != null) {
            resolverIds.add(resolverId);
        }

        for (UUID uid : userIds) {
            UserResponse user = fetchUserInfo(uid);
            var complains = repository.findAllByFilters(uid, null, status, null);
            for (Complain c : complains) {
                UserResponse resolver = fetchUserInfo(c.getResolverId());
                result.add(mapToResponse(c, user, resolver));
            }
        }

        for (UUID rid : resolverIds) {
            UserResponse resolver = fetchUserInfo(rid);
            var complains = repository.findAllByFilters(null, rid, status, null);
            for (Complain c : complains) {
                UserResponse user = fetchUserInfo(c.getUserId());
                result.add(mapToResponse(c, user, resolver));
            }
        }

        var allComplains = repository.findAllByFilters(null, null, status, keyword);
        for (Complain c : allComplains) {
            UserResponse user = fetchUserInfo(c.getUserId());
            UserResponse resolver = fetchUserInfo(c.getResolverId());
            result.add(mapToResponse(c, user, resolver));
        }

        // Phân trang sau khi map luôn
        List<ComplainResponse> paginatedResult = result.stream()
                .distinct()
                .skip(skipCount)
                .limit(maxResultCount)
                .collect(Collectors.toList());

        return BaseGetAllResponse.<ComplainResponse>builder()
                .data(paginatedResult)
                .totalRecords(result.size())
                .build();
    }

    public BaseGetAllResponse<ComplainResponse> getAllUnresolved(ComplainGetAllRequest request) {
        int skipCount = Optional.ofNullable(request.getSkipCount()).orElse(0);
        int maxResultCount = Optional.ofNullable(request.getMaxResultCount()).orElse(10);

        String keyword = normalize(request.getKeyword());
        String userFullName = normalize(request.getUserFullName());
        UUID userId = request.getUserId();

        List<UUID> userIds = new ArrayList<>();

        if (userFullName != null) {
            var userResp = userClient.getAllUsers(
                    0, 1000, keyword, null, null, userFullName,
                    null, null, null);

            userIds = userResp.getResult().getData().stream()
                    .map(UserResponse::getId)
                    .collect(Collectors.toList());
        } else if (userId != null) {
            userIds.add(userId);
        }

        List<ComplainResponse> result = new ArrayList<>();

        for (UUID uid : userIds) {
            var complains = repository.findAllUnresolved(uid, keyword);
            UserResponse user = fetchUserInfo(uid);

            for (Complain c : complains) {
                UserResponse resolver = fetchUserInfo(c.getResolverId());
                result.add(mapToResponse(c, user, resolver));
            }
        }

        var complains = repository.findAllUnresolved(null, keyword);
        for (Complain c : complains) {
            UserResponse user = fetchUserInfo(c.getUserId());
            UserResponse resolver = fetchUserInfo(c.getResolverId());
            result.add(mapToResponse(c, user, resolver));
        }

        // Phân trang
        List<ComplainResponse> paginatedResult = result.stream()
                .distinct()
                .skip(skipCount)
                .limit(maxResultCount)
                .collect(Collectors.toList());

        return BaseGetAllResponse.<ComplainResponse>builder()
                .data(paginatedResult)
                .totalRecords(result.size())
                .build();
    }

    private String normalize(String input) {
        return (input == null || input.isBlank()) ? null : input.trim();
    }

    // Hàm chỉ dùng để map, không gọi API
    private ComplainResponse mapToResponse(
            Complain entity,
            UserResponse user,
            UserResponse resolver) {

        return ComplainResponse.builder()
                .userId(entity.getUserId())
                .userName(user != null ? user.getUserName() : null)
                .userFullName(user != null ? user.getFullName() : null)
                .content(entity.getContent())
                .resolverId(entity.getResolverId())
                .resolverName(resolver != null ? resolver.getUserName() : null)
                .resolverFullName(resolver != null ? resolver.getFullName() : null)
                .resolverPhoneNumber(resolver != null ? resolver.getPhoneNumber() : null)
                .status(entity.getStatus())
                .resolvingNote(entity.getResolvingNote())
                .timeSubmit(entity.getTimeSubmit())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    private UserResponse fetchUserInfo(UUID userId) {
        if (userId == null) {
            return null;
        }
        String cacheKey = CacheKeys.USER_PREFIX + userId;
        UserResponse user = redisService.get(cacheKey, UserResponse.class);
        if (user == null) {
            ApiResponse<UserResponse> response = userClient.getUserById(userId);
            if (response != null && response.isSuccess()) {
                user = response.getResult();
            } else {
                throw new AppException(ErrorCode.NOT_FOUND, "User not found");
            }
        }
        return user;
    }
}
