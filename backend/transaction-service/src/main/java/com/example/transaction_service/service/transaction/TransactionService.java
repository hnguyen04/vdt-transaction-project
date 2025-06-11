package com.example.transaction_service.service.transaction;

import com.example.transaction_service.client.UserClient;
import com.example.transaction_service.common.constants.CacheKeys;
import com.example.transaction_service.common.constants.RoleEnum;
import com.example.transaction_service.common.constants.TransactionStatusEnum;
import com.example.transaction_service.common.constants.TransactionTypeEnum;
import com.example.transaction_service.common.dto.response.ApiResponse;
import com.example.transaction_service.common.dto.response.BaseGetAllResponse;
import com.example.transaction_service.common.exception.AppException;
import com.example.transaction_service.common.exception.ErrorCode;
import com.example.transaction_service.dto.transaction.TransactionCreateRequest;
import com.example.transaction_service.dto.transaction.TransactionGetAllRequest;
import com.example.transaction_service.dto.transaction.TransactionResponse;
import com.example.transaction_service.dto.transaction.TransactionUpdateRequest;
import com.example.transaction_service.dto.user.UserResponse;
import com.example.transaction_service.entity.Transaction;
import com.example.transaction_service.repository.TransactionRepository;
import com.example.transaction_service.service.RedisService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserClient userClient;
    private final RedisService redisService;

    private static final long CACHE_DURATION_MINUTES = 5;

    public TransactionService(TransactionRepository transactionRepository, UserClient userClient, RedisService redisService) {
        this.transactionRepository = transactionRepository;
        this.userClient = userClient;
        this.redisService = redisService;
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN', 'STAFF')")
    public BaseGetAllResponse<TransactionResponse> getAll(TransactionGetAllRequest request) {
        int skipCount = Optional.ofNullable(request.getSkipCount()).orElse(0);
        int maxResultCount = Optional.ofNullable(request.getMaxResultCount()).orElse(10);

        String keyword = normalize(request.getKeyword());
        String code = normalize(request.getCode());
        String bankCode = normalize(request.getBankCode());
        TransactionStatusEnum status = request.getStatus();
        TransactionTypeEnum type = request.getType();
        String sourceAccount = normalize(request.getSourceAccount());
        String destinationAccount = normalize(request.getDestinationAccount());
        UUID userId = request.getUserId() != null ? request.getUserId() : null;

        resolveUserIdFromUserFields(request);

        String cacheKey = buildCacheKey(request);
        boolean shouldUseCache = request.getSkipCount() == 0 && request.getMaxResultCount() <= 100;

        if (shouldUseCache) {
            List<TransactionResponse> cachedList = redisService.getList(cacheKey, TransactionResponse.class);
            if (cachedList != null && !cachedList.isEmpty()) {
                return BaseGetAllResponse.<TransactionResponse>builder()
                        .data(paginate(cachedList, skipCount, maxResultCount))
                        .totalRecords(cachedList.size())
                        .build();
            }
        }

        List<Transaction> transactionList = transactionRepository.findAllWithFilters(
                userId,
                code,
                status,
                type,
                sourceAccount,
                destinationAccount,
                bankCode,
                keyword
        );

        List<TransactionResponse> responseList = transactionList.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());

        if (shouldUseCache) {
            redisService.setList(cacheKey, responseList, CACHE_DURATION_MINUTES, TimeUnit.MINUTES);
        }

        return BaseGetAllResponse.<TransactionResponse>builder()
                .data(paginate(responseList, skipCount, maxResultCount))
                .totalRecords(responseList.size())
                .build();
    }


    public TransactionResponse create(TransactionCreateRequest request) {
        Transaction transaction = Transaction.builder()
                .id(UUID.randomUUID())
                .userId(request.getUserId())
                .code(generateCode())
                .commitTime(Optional.ofNullable(request.getCommitTime()).orElse(LocalDateTime.now()))
                .amount(request.getAmount())
                .status(request.getStatus())
                .type(request.getType())
                .sourceAccount(request.getSourceAccount())
                .destinationAccount(request.getDestinationAccount())
                .bankCode(request.getBankCode())
                .build();

        transactionRepository.save(transaction);
        removeTransactionCache(transaction);
        return mapToResponse(transaction);

    }

    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN', 'STAFF')")
    @Transactional
    public TransactionResponse update(TransactionUpdateRequest request) {
        Transaction transaction = transactionRepository.findById(request.getId())
                .orElseThrow(() -> new AppException(ErrorCode.BAD_REQUEST, "Transaction not found"));

        if (request.getAmount() != null && request.getAmount().compareTo(BigDecimal.ZERO) > 0) {
            transaction.setAmount(request.getAmount());
        }
        if (request.getStatus() != null) {
            transaction.setStatus(request.getStatus());
        }

        System.out.println("Transaction hash before update: " + transaction.hashCode());
        transactionRepository.save(transaction);
        System.out.println("Transaction hash after update: " + transaction.hashCode());

        removeTransactionCache(transaction);
        return mapToResponse(transaction);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN', 'STAFF')")
    public void delete(UUID id) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.BAD_REQUEST, "Transaction not found"));

        transactionRepository.softDeleteById(id);
        removeTransactionCache(transaction);
    }

    public TransactionResponse getById(UUID id) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.BAD_REQUEST, "Transaction not found"));
        System.out.println("Transaction hash " + transaction.hashCode());

        return mapToResponse(transaction);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN', 'STAFF')")
    @Transactional
    public TransactionResponse approve(UUID transactionId) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new AppException(ErrorCode.BAD_REQUEST, "Transaction not found"));

        if (transaction.getStatus() != TransactionStatusEnum.PENDING) {
            throw new AppException(ErrorCode.BAD_REQUEST, "Transaction is not in PENDING status");
        }

        transaction.setStatus(TransactionStatusEnum.SUCCESS);
        transaction.setUpdatedAt(LocalDateTime.now());
        transactionRepository.save(transaction);
        removeTransactionCache(transaction);
        return mapToResponse(transaction);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN', 'STAFF')")
    @Transactional
    public TransactionResponse reject(UUID transactionId) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new AppException(ErrorCode.BAD_REQUEST, "Transaction not found"));

        if (transaction.getStatus() != TransactionStatusEnum.PENDING) {
            throw new AppException(ErrorCode.BAD_REQUEST, "Transaction is not in PENDING status");
        }

        transaction.setStatus(TransactionStatusEnum.FAILED);
        transaction.setUpdatedAt(LocalDateTime.now());
        transactionRepository.save(transaction);
        removeTransactionCache(transaction);
        return mapToResponse(transaction);
    }

    private UserResponse getUserFromCacheOrFeign(UUID userId) {
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

    private String buildCacheKey(TransactionGetAllRequest request) {
        Map<String, Object> filteredParams = new LinkedHashMap<>();

        if (request.getCode() != null) filteredParams.put("code", request.getCode());
        if (request.getStatus() != null) filteredParams.put("status", request.getStatus());
        if (request.getType() != null) filteredParams.put("type", request.getType());
        if (request.getSourceAccount() != null) filteredParams.put("sourceAccount", request.getSourceAccount());
        if (request.getDestinationAccount() != null) filteredParams.put("destinationAccount", request.getDestinationAccount());
        if (request.getBankCode() != null) filteredParams.put("bankCode", request.getBankCode());
        if (request.getKeyword() != null) filteredParams.put("keyword", request.getKeyword());
        if (request.getSkipCount() != null) filteredParams.put("skipCount", request.getSkipCount());
        if (request.getMaxResultCount() != null) filteredParams.put("maxResultCount", request.getMaxResultCount());

        String hash = md5Stable(filteredParams.toString());

        if (request.getUserId() != null) {
            return "transaction:user:" + request.getUserId() + ":filters:" + hash;
        } else if (request.getStatus() != null) {
            return "transaction:status:" + request.getStatus() + ":filters:" + hash;
        } else if (request.getType() != null) {
            return "transaction:type:" + request.getType() + ":filters:" + hash;
        } else {
            return "transaction:filters:" + hash;
        }
    }

    private static String md5Stable(Object object) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, true); // ổn định thứ tự
            String json = mapper.writeValueAsString(object);
            return DigestUtils.md5Hex(json);
        } catch (JsonProcessingException e) {
            throw new AppException(ErrorCode.INTERNAL_SERVER_ERROR, "Failed to generate MD5 hash for object" + e.getMessage());
        }
    }

    private void removeTransactionCache(Transaction transaction) {
        redisService.removeKeysByPrefix("transaction:user:" + transaction.getUserId() + ":filters:");

    // Toàn bộ cache theo status/type nếu biết
        redisService.removeKeysByPrefix("transaction:status:" + transaction.getStatus() + ":filters:");
        redisService.removeKeysByPrefix("transaction:type:" + transaction.getType() + ":filters:");

    // Cuối cùng, xóa cache không định danh
        redisService.removeKeysByPrefix("transaction:filters:");
    }

    private <T> List<T> paginate(List<T> list, int skip, int limit) {
        int fromIndex = Math.min(skip, list.size());
        int toIndex = Math.min(fromIndex + limit, list.size());
        return list.subList(fromIndex, toIndex);
    }

    private String generateCode() {
        return String.format("%08d", new Random().nextInt(100_000_000));
    }

    private void resolveUserIdFromUserFields(TransactionGetAllRequest request) {
        if (request.getUserId() == null && (
                request.getUserCode() != null ||
                        request.getPhoneNumber() != null ||
                        request.getUserName() != null ||
                        request.getCmnd() != null)) {

            List<UserResponse> users = userClient.getAllUsers(
                    0, 1000, null, RoleEnum.USER,
                    request.getUserName(), null,
                    request.getPhoneNumber(), request.getCmnd(), request.getUserCode()
            ).getResult().getData();
            if (users.size() == 1) {
                request.setUserId(users.getFirst().getId());
            }
        }
    }

    private TransactionResponse mapToResponse(Transaction tx) {
        UserResponse user = getUserFromCacheOrFeign(tx.getUserId());
        return TransactionResponse.builder()
                .id(tx.getId())
                .userId(tx.getUserId())
                .code(tx.getCode())
                .commitTime(tx.getCommitTime())
                .amount(tx.getAmount())
                .status(tx.getStatus())
                .type(tx.getType())
                .sourceAccount(tx.getSourceAccount())
                .destinationAccount(tx.getDestinationAccount())
                .bankCode(tx.getBankCode())
                .userName(user.getUserName())
                .fullName(user.getFullName())
                .phoneNumber(user.getPhoneNumber())
                .cmnd(user.getCmnd())
                .userCode(user.getCode())
                .createdAt(tx.getCreatedAt())
                .updatedAt(tx.getUpdatedAt())
                .build();
    }

    private String normalize(String input) {
        return (input == null || input.isBlank()) ? null : input.trim();
    }

    private List<TransactionResponse> buildTransactionResponses(List<Transaction> transactions) {
        List<TransactionResponse> responses = new ArrayList<>();
        for (Transaction tx : transactions) {
            UUID userId = tx.getUserId();
            UserResponse user = getUserFromCacheOrFeign(userId);
            responses.add(TransactionMapper.toResponse(tx, user));
        }
        return responses;
    }

}
