package com.example.complain_service.service.Complain;

import com.example.complain_service.client.TransactionClient;
import com.example.complain_service.client.UserClient;
import com.example.complain_service.common.constants.CacheKeys;
import com.example.complain_service.common.constants.ComplainStatusEnum;
import com.example.complain_service.common.constants.RoleEnum;
import com.example.complain_service.common.dto.response.ApiResponse;
import com.example.complain_service.common.dto.response.BaseGetAllResponse;
import com.example.complain_service.common.exception.AppException;
import com.example.complain_service.common.exception.ErrorCode;
import com.example.complain_service.dto.complain.*;
import com.example.complain_service.dto.transaction.TransactionResponse;
import com.example.complain_service.dto.user.UserResponse;
import com.example.complain_service.entity.Complain;
import com.example.complain_service.repository.ComplainRepository;
import com.example.complain_service.service.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ComplainService {

    private final ComplainRepository repository;
    private final UserClient userClient;
    private final TransactionClient transactionClient;
    private final RedisService redisService;

    @Transactional
    public ComplainResponse create(ComplainCreateRequest request) {
        var entity = com.example.complain_service.entity.Complain.builder()
                .transactionId(request.getUserId())
                .content(request.getContent())
                .status(ComplainStatusEnum.PENDING)
                .timeSubmit(LocalDateTime.now())
                .build();

        entity = repository.save(entity);

        // Lấy dữ liệu user/resolver
        TransactionResponse transactionData = transactionClient.getTransactionById(entity.getTransactionId())
                .getResult();
        UserResponse userData = fetchUserInfo(transactionData.getUserId());
        UserResponse resolverData = fetchUserInfo(entity.getResolverId());

        return mapToResponse(entity, userData, resolverData, transactionData);
    }

    public ComplainResponse getById(UUID id) {
        var entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Complain not found with id: " + id));

        TransactionResponse transactionData = transactionClient.getTransactionById(entity.getTransactionId())
                .getResult();
        var userData = fetchUserInfo(transactionData.getUserId());
        var resolverData = fetchUserInfo(entity.getResolverId());

        return mapToResponse(entity, userData, resolverData, transactionData);
    }

    @Transactional
    public ComplainResponse update(ComplainUpdateRequest request) {
        var entity = repository.findById(request.getId())
                .orElseThrow(() -> new AppException(ErrorCode.BAD_REQUEST, "Complain not found with id: " + request.getId()));

        if (request.getContent() != null) {
            entity.setContent(request.getContent());
        }
        entity = repository.save(entity);

        TransactionResponse transactionData = transactionClient.getTransactionById(entity.getTransactionId())
                .getResult();
        var userData = fetchUserInfo(transactionData.getUserId());
        var resolverData = fetchUserInfo(entity.getResolverId());

        return mapToResponse(entity, userData, resolverData, transactionData);
    }

    public void delete(UUID id) {
        var entity = repository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.BAD_REQUEST, "Complain not found with id: " + id));
    }

    @Transactional
    public ComplainResponse claim(ComplainAssignRequest request) {
        var entity = repository.findById(request.getId())
                .orElseThrow(() -> new AppException(ErrorCode.BAD_REQUEST, "Complain not found with id: " + request.getId()));

        entity.setResolverId(request.getResolverId());
        entity = repository.save(entity);

        TransactionResponse transactionData = transactionClient.getTransactionById(entity.getTransactionId())
                .getResult();
        var user = fetchUserInfo(transactionData.getUserId());
        var resolver = fetchUserInfo(entity.getResolverId());

        return mapToResponse(entity, user, resolver, transactionData);
    }

    @Transactional
    public ComplainResponse assign(ComplainAssignRequest request) {
        var entity = repository.findById(request.getId())
                .orElseThrow(() -> new AppException(ErrorCode.BAD_REQUEST, "Complain not found with id: " + request.getId()));

        entity.setResolverId(request.getResolverId());
        entity = repository.save(entity);

        TransactionResponse transactionData = transactionClient.getTransactionById(entity.getTransactionId())
                .getResult();
        var user = fetchUserInfo(transactionData.getUserId());
        var resolver = fetchUserInfo(entity.getResolverId());

        return mapToResponse(entity, user, resolver, transactionData);
    }

    @Transactional
    public ComplainResponse resolve(ComplainAssignRequest request) {
        var entity = repository.findById(request.getId())
                .orElseThrow(() -> new AppException(ErrorCode.BAD_REQUEST, "Complain not found with id: " + request.getId()));


        entity.setResolverId(request.getResolverId());
        entity.setStatus(ComplainStatusEnum.RESOLVED);
        entity = repository.save(entity);

        TransactionResponse transactionData = transactionClient.getTransactionById(entity.getTransactionId())
                .getResult();
        var user = fetchUserInfo(transactionData.getUserId());
        var resolver = fetchUserInfo(entity.getResolverId());

        return mapToResponse(entity, user, resolver, transactionData);
    }

    @Transactional
    public ComplainResponse note(ComplainNoteRequest request) {
        var entity = repository.findById(request.getId())
                .orElseThrow(() -> new AppException(ErrorCode.BAD_REQUEST, "Complain not found with id: " + request.getId()));

        entity.setResolvingNote(request.getNote());
        entity = repository.save(entity);

        TransactionResponse transactionData = transactionClient.getTransactionById(entity.getTransactionId())
                .getResult();
        var user = fetchUserInfo(transactionData.getUserId());
        var resolver = fetchUserInfo(entity.getResolverId());

        return mapToResponse(entity, user, resolver, transactionData);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN', 'STAFF')")
    public BaseGetAllResponse<ComplainResponse> getAll(ComplainGetAllRequest request) {
        int skipCount = Optional.ofNullable(request.getSkipCount()).orElse(0);
        int maxResultCount = Optional.ofNullable(request.getMaxResultCount()).orElse(10);

        String keyword = normalize(request.getKeyword());
        String userFullName = normalize(request.getUserFullName());
        String resolverFullName = normalize(request.getResolverFullName());
        ComplainStatusEnum status = request.getStatus();
        String transactionCode = normalize(request.getTransactionCode());
        UUID userId = request.getUserId() != null ? request.getUserId() : null;
        UUID resolverId = request.getResolverId() != null ? request.getResolverId() : null;
        UUID transactionId = request.getTransactionId() != null ? request.getTransactionId() : null;

        List<ComplainResponse> result = new ArrayList<>();

        if (transactionCode != null && transactionId == null) {
            // Nếu có transactionCode thì lấy transactionId từ transaction service
            ApiResponse<BaseGetAllResponse<TransactionResponse>> txResp = transactionClient.getAllTransactions(
                    0, 1000, null, null, transactionCode, null,
                    null, null, null, null, null, null, null, null);

            if (txResp.isSuccess() && !txResp.getResult().getData().isEmpty()) {
                transactionId = txResp.getResult().getData().getFirst().getId();
            }
        }

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

        Map<UUID, UserResponse> userCache = new HashMap<>();
        Map<UUID, UserResponse> resolverCache = new HashMap<>();
        Map<UUID, TransactionResponse> transactionCache = new HashMap<>();

// Helper methods
        Function<UUID, UserResponse> getUser = id ->
                id == null ? null : userCache.computeIfAbsent(id, this::fetchUserInfo);

        Function<UUID, UserResponse> getResolver = id ->
                id == null ? null : resolverCache.computeIfAbsent(id, this::fetchUserInfo);

        Function<UUID, TransactionResponse> getTransaction = id ->
                transactionCache.computeIfAbsent(id,
                        tid -> transactionClient.getTransactionById(tid).getResult());

        if (transactionId == null) {
// ====== 1. Complains theo userIds ======
            for (UUID uid : userIds) {
                UserResponse user = getUser.apply(uid);

                List<TransactionResponse> transactions = transactionClient.getAllTransactions(
                        0, 1000, null, uid, null, null,
                        null, null, null, null, null, null, null, null
                ).getResult().getData();

                for (TransactionResponse tx : transactions) {
                    UUID txId = tx.getId();
                    transactionCache.put(txId, tx);

                    List<Complain> complains = repository.findAllByFilters(txId, null, status, null);
                    for (Complain c : complains) {
                        UserResponse resolver = getResolver.apply(c.getResolverId());
                        result.add(mapToResponse(c, user, resolver, tx));
                    }
                }
            }

        }
        if (resolverId == null) {
// ====== 2. Complains theo resolverIds ======
            for (UUID rid : resolverIds) {
                UserResponse resolver = getResolver.apply(rid);

                var complains = repository.findAllByFilters(transactionId, rid, status, null);
                for (Complain c : complains) {
                    TransactionResponse tx = getTransaction.apply(c.getTransactionId());
                    UserResponse user = getUser.apply(tx.getUserId());
                    result.add(mapToResponse(c, user, resolver, tx));
                }
            }
        }


// ====== 3. Complains theo keyword ======
        var allComplains = repository.findAllByFilters(transactionId, resolverId, status, keyword);
        for (Complain c : allComplains) {
            TransactionResponse tx = getTransaction.apply(c.getTransactionId());
            UserResponse user = getUser.apply(tx.getUserId());
            UserResponse resolver = getResolver.apply(c.getResolverId());
            result.add(mapToResponse(c, user, resolver, tx));
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
        String transactionCode = normalize(request.getTransactionCode());
        String keyword = normalize(request.getKeyword());
        String userFullName = normalize(request.getUserFullName());
        UUID userId = request.getUserId();
        UUID transactionId = request.getTransactionId() != null ? request.getTransactionId() : null;

        if (transactionCode != null && transactionId == null) {
            // Nếu có transactionCode thì lấy transactionId từ transaction service
            ApiResponse<BaseGetAllResponse<TransactionResponse>> txResp = transactionClient.getAllTransactions(
                    0, 1000, null, null, transactionCode, null,
                    null, null, null, null, null, null, null, null);

            if (txResp.isSuccess() && !txResp.getResult().getData().isEmpty()) {
                transactionId = txResp.getResult().getData().getFirst().getId();
            }
        }

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

        Map<UUID, UserResponse> userCache = new HashMap<>();
        Map<UUID, UserResponse> resolverCache = new HashMap<>();
        Map<UUID, TransactionResponse> transactionCache = new HashMap<>();

        Function<UUID, UserResponse> getUser = id ->
                id == null ? null : userCache.computeIfAbsent(id, this::fetchUserInfo);

        Function<UUID, UserResponse> getResolver = id ->
                id == null ? null : resolverCache.computeIfAbsent(id, this::fetchUserInfo);

        Function<UUID, TransactionResponse> getTransaction = id ->
                transactionCache.computeIfAbsent(id,
                        tid -> transactionClient.getTransactionById(tid).getResult());

        if (transactionId == null) {

// ====== 1. Theo danh sách userIds ======
            for (UUID uid : userIds) {
                // Lấy tất cả transaction của user
                List<TransactionResponse> transactions = transactionClient.getAllTransactions(
                        0, 1000, null, uid, null, null,
                        null, null, null, null, null, null, null, null
                ).getResult().getData();

                UserResponse user = getUser.apply(uid);

                for (TransactionResponse tx : transactions) {
                    UUID txId = tx.getId();
                    transactionCache.put(txId, tx);

                    List<Complain> complains = repository.findAllUnresolved(txId, keyword);
                    for (Complain c : complains) {
                        UserResponse resolver = getResolver.apply(c.getResolverId());
                        result.add(mapToResponse(c, user, resolver, tx));
                    }
                }
            }
        }
// ====== 2. Các complain chưa resolve còn lại (không thuộc userIds cụ thể) ======
        List<Complain> complains = repository.findAllUnresolved(transactionId, keyword);
        for (Complain c : complains) {
            TransactionResponse tx = getTransaction.apply(c.getTransactionId());
            UserResponse user = getUser.apply(tx.getUserId());
            UserResponse resolver = getResolver.apply(c.getResolverId());

            result.add(mapToResponse(c, user, resolver, tx));
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
            UserResponse resolver,
            TransactionResponse transaction
    ) {

        return ComplainResponse.builder()
                .id(entity.getId())
                .transactionId(entity.getTransactionId())
                .transactionCode(transaction != null ? transaction.getCode() : null)
                .userId(user.getId())
                .userName(user.getUserName())
                .userFullName(user.getFullName())
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
