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
    private final ComplainRepository complainRepository;

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
        UserResponse resolverData = fetchUserInfo(entity.getResolverId());

        return mapToResponse(entity, resolverData, transactionData);
    }

    public ComplainResponse getById(UUID id) {
        var entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Complain not found with id: " + id));

        TransactionResponse transactionData = transactionClient.getTransactionById(entity.getTransactionId())
                .getResult();
        var resolverData = fetchUserInfo(entity.getResolverId());

        return mapToResponse(entity, resolverData, transactionData);
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
        var resolverData = fetchUserInfo(entity.getResolverId());

        return mapToResponse(entity, resolverData, transactionData);
    }

    public void delete(UUID id) {
        var entity = repository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.BAD_REQUEST, "Complain not found with id: " + id));
        complainRepository.softDeleteById(id);
    }

    @Transactional
    public ComplainResponse claim(ComplainAssignRequest request) {
        var entity = repository.findById(request.getId())
                .orElseThrow(() -> new AppException(ErrorCode.BAD_REQUEST, "Complain not found with id: " + request.getId()));

        entity.setResolverId(request.getResolverId());
        entity = repository.save(entity);

        TransactionResponse transactionData = transactionClient.getTransactionById(entity.getTransactionId())
                .getResult();
        var resolver = fetchUserInfo(entity.getResolverId());

        return mapToResponse(entity, resolver, transactionData);
    }

    @Transactional
    public ComplainResponse assign(ComplainAssignRequest request) {
        var entity = repository.findById(request.getId())
                .orElseThrow(() -> new AppException(ErrorCode.BAD_REQUEST, "Complain not found with id: " + request.getId()));

        entity.setResolverId(request.getResolverId());
        entity = repository.save(entity);

        TransactionResponse transactionData = transactionClient.getTransactionById(entity.getTransactionId())
                .getResult();
        var resolver = fetchUserInfo(entity.getResolverId());

        return mapToResponse(entity, resolver, transactionData);
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
        var resolver = fetchUserInfo(entity.getResolverId());

        return mapToResponse(entity, resolver, transactionData);
    }

    @Transactional
    public ComplainResponse note(ComplainNoteRequest request) {
        var entity = repository.findById(request.getId())
                .orElseThrow(() -> new AppException(ErrorCode.BAD_REQUEST, "Complain not found with id: " + request.getId()));

        entity.setResolvingNote(request.getNote());
        entity = repository.save(entity);

        TransactionResponse transactionData = transactionClient.getTransactionById(entity.getTransactionId())
                .getResult();
        var resolver = fetchUserInfo(entity.getResolverId());

        return mapToResponse(entity, resolver, transactionData);
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

        final boolean shouldGetAllResolver = resolverId != null
                || resolverFullName != null;

        final boolean shouldGetAllTransaction = transactionId != null
                || userId != null
                || userFullName != null
                || transactionCode != null;

        // 2. Gọi userClient.getAllUsers nếu cần
        List<UserResponse> userList = new ArrayList<>();
        if (shouldGetAllResolver) {
            ApiResponse<BaseGetAllResponse<UserResponse>> userRes =
                    userClient.getAllUsers(
                            0, Integer.MAX_VALUE, keyword, null,
                            null, resolverFullName, null, null, null);
            if (userRes != null && userRes.isSuccess()) {
                userList = userRes.getResult().getData();
            }
        }

        // 3. Gọi transactionClient.getAllTransactions nếu cần
        List<TransactionResponse> transactionList = new ArrayList<>();
        if (shouldGetAllTransaction) {
            ApiResponse<BaseGetAllResponse<TransactionResponse>> transRes =
                    transactionClient.getAllTransactions(
                            0, 1000, null, userId, transactionCode, null, null, null,
                            null, null, null, null, null, null, userFullName
                    );
            System.out.println("Transaction List Size: " + transactionList);

            if (transRes != null && transRes.isSuccess()) {
                transactionList = transRes.getResult().getData();
            }
        }


        // 4. Lấy danh sách Complain từ DB
        List<Complain> complains = complainRepository.findAllByFilters(
                transactionId,
                resolverId,
                status,
                keyword
        );

        Map<UUID, TransactionResponse> transactionMap = transactionList.stream()
                .collect(Collectors.toMap(TransactionResponse::getId, t -> t));

        Map<UUID, UserResponse> userMap = userList.stream()
                .collect(Collectors.toMap(UserResponse::getId, u -> u));

        // 5. Ghép dữ liệu và map sang response
        List<ComplainResponse> responses = complains.stream()
                .skip(skipCount)
                .limit(maxResultCount)
                .map(entity -> {
                    final TransactionResponse transaction = shouldGetAllTransaction
                            ? transactionMap.get(entity.getTransactionId())
                            : fetchTransactionInfo(entity.getTransactionId());

                    final UserResponse resolver = shouldGetAllResolver
                            ? userMap.get(entity.getResolverId())
                            : fetchUserInfo(entity.getResolverId());

                    // Nếu đang lọc mà không tìm thấy transaction hoặc resolver thì bỏ qua
                    if ((shouldGetAllTransaction && transaction == null) ||
                            (shouldGetAllResolver && resolver == null)) {
                        return null;
                    }

                    return mapToResponse(entity, resolver, transaction);
                })
                .filter(Objects::nonNull)
                .toList();
        return BaseGetAllResponse.<ComplainResponse>builder()
                .data(responses)
                .totalRecords(complains.size())
                .build();
    }

    public BaseGetAllResponse<ComplainResponse> getAllUnresolved(ComplainGetAllRequest request) {
        int skipCount = Optional.ofNullable(request.getSkipCount()).orElse(0);
        int maxResultCount = Optional.ofNullable(request.getMaxResultCount()).orElse(10);

        String keyword = normalize(request.getKeyword());
        String userFullName = normalize(request.getUserFullName());
        String transactionCode = normalize(request.getTransactionCode());
        UUID userId = request.getUserId();
        UUID transactionId = request.getTransactionId();

        final boolean shouldGetAllTransaction = transactionId != null
                || userId != null
                || userFullName != null
                || transactionCode != null;

        // 1. Gọi transactionClient.getAllTransactions nếu cần
        List<TransactionResponse> transactionList = new ArrayList<>();
        if (shouldGetAllTransaction) {
            ApiResponse<BaseGetAllResponse<TransactionResponse>> transRes =
                    transactionClient.getAllTransactions(
                            0, Integer.MAX_VALUE, keyword,
                            userId, transactionCode,
                            null, null, null,
                            null, null, null, null,
                            null, null, userFullName
                    );
            if (transRes != null && transRes.isSuccess()) {
                transactionList = transRes.getResult().getData();
            }
        }

        // 2. Lấy danh sách Complain từ DB
        List<Complain> complains = complainRepository.findAllUnresolved(
                transactionId,
                keyword
        );

        final Map<UUID, TransactionResponse> transactionMap = transactionList.stream()
                .collect(Collectors.toMap(TransactionResponse::getId, t -> t));

        // 3. Ghép dữ liệu và map sang response
        List<ComplainResponse> responses = complains.stream()
                .skip(skipCount)
                .limit(maxResultCount)
                .map(entity -> {
                    final TransactionResponse transaction = shouldGetAllTransaction
                            ? transactionMap.get(entity.getTransactionId())
                            : fetchTransactionInfo(entity.getTransactionId());

                    // Nếu đang lọc mà không tìm thấy transaction thì bỏ qua
                    if (shouldGetAllTransaction && transaction == null) {
                        return null;
                    }

                    return mapToResponse(entity, null, transaction);
                })
                .filter(Objects::nonNull)
                .toList();

        return BaseGetAllResponse.<ComplainResponse>builder()
                .data(responses)
                .totalRecords(complains.size())
                .build();
    }

    private String normalize(String input) {
        return (input == null || input.isBlank()) ? null : input.trim();
    }

    // Hàm chỉ dùng để map, không gọi API
    private ComplainResponse mapToResponse(
            Complain entity,
            UserResponse resolver,
            TransactionResponse transaction
    ) {

        return ComplainResponse.builder()
                .id(entity.getId())
                .transactionId(entity.getTransactionId())
                .transactionCode(transaction != null ? transaction.getCode() : null)
                .userId(transaction.getUserId())
                .userName(transaction.getUserName())
                .userFullName(transaction.getFullName())
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


    private TransactionResponse fetchTransactionInfo(UUID transactionId) {
        if (transactionId == null) return null;
        ApiResponse<TransactionResponse> res = transactionClient.getTransactionById(transactionId);
        if (res != null && res.isSuccess()) {
            return res.getResult();
        } else {
            throw new AppException(ErrorCode.NOT_FOUND, "Transaction not found");
        }
    }
}
