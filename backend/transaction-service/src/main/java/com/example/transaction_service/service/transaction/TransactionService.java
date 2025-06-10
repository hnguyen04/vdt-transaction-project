package com.example.transaction_service.service.transaction;

import com.example.transaction_service.client.UserClient;
import com.example.transaction_service.common.constants.CacheKeys;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
@Transactional(readOnly = true)
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserClient userClient;
    private final RedisService redisService;

    public TransactionService(TransactionRepository transactionRepository, UserClient userClient, RedisService redisService) {
        this.transactionRepository = transactionRepository;
        this.userClient = userClient;
        this.redisService = redisService;
    }

    public BaseGetAllResponse<TransactionResponse> getAll(TransactionGetAllRequest request) {
        int skipCount = Optional.ofNullable(request.getSkipCount()).orElse(0);
        int maxResultCount = Optional.ofNullable(request.getMaxResultCount()).orElse(10);

        String keyword = normalize(request.getKeyword());
        String userName = normalize(request.getUserName());
        String phoneNumber = normalize(request.getPhoneNumber());
        String cmnd = normalize(request.getCmnd());
        String userCode = normalize(request.getUserCode());
        String code = normalize(request.getCode());
        String bankCode = normalize(request.getBankCode());
        TransactionStatusEnum status = request.getStatus();
        TransactionTypeEnum type = request.getType();
        String sourceAccount = normalize(request.getSourceAccount());
        String destinationAccount = normalize(request.getDestinationAccount());

        boolean hasUserFilter = userName != null || phoneNumber != null || cmnd != null || userCode != null;

        List<TransactionResponse> finalResults = new ArrayList<>();

        if (keyword != null || hasUserFilter) {
            ApiResponse<BaseGetAllResponse<UserResponse>> response = userClient.getAllUsers(
                    0, Integer.MAX_VALUE,
                    keyword, null, userName, null, phoneNumber, cmnd, userCode
            );

            List<UserResponse> users = Collections.emptyList();
            if (response != null && response.isSuccess() && response.getResult() != null) {
                users = response.getResult().getData();
            }

            if (users.isEmpty()) return BaseGetAllResponse.<TransactionResponse>builder()
                    .data(Collections.emptyList())
                    .totalRecords(0)
                    .build();

            if (users.size() == 1) {
                redisService.set(CacheKeys.USER_PREFIX + users.getFirst().getId(), users.getFirst(), 10, TimeUnit.MINUTES);
            }

            for (UserResponse user : users) {
                List<Transaction> transactions;

                if (keyword == null) {
                    String transactionCacheKey = CacheKeys.TRANSACTIONS_PREFIX + user.getId();
                    transactions = redisService.getList(transactionCacheKey, Transaction.class);

                    if (transactions == null) {
                        transactions = transactionRepository.findAllWithFilters(
                                user.getId(), code, status, type, sourceAccount, destinationAccount, bankCode, null
                        );
                        redisService.setList(transactionCacheKey, transactions, 5, TimeUnit.MINUTES);
                    }
                } else {
                    // Bỏ qua cache nếu có keyword
                    transactions = transactionRepository.findAllWithFilters(
                            user.getId(), code, status, type, sourceAccount, destinationAccount, bankCode, null
                    );

                    List<Transaction> transactionsWithKeyword = transactionRepository.findAllWithFilters(
                            null, code, status, type, sourceAccount, destinationAccount, bankCode, keyword
                    );

                    // Gộp và loại trùng
                    Set<Transaction> mergedTransactionsSet = new LinkedHashSet<>();
                    mergedTransactionsSet.addAll(transactions);
                    mergedTransactionsSet.addAll(transactionsWithKeyword);

                    transactions = new ArrayList<>(mergedTransactionsSet);
                }

                for (Transaction tx : transactions) {
                    finalResults.add(TransactionMapper.toResponse(tx, user));
                }
            }

        } else {
            List<TransactionResponse> transactionResponses;
            boolean useTypeCache = type != null && status == null;
            boolean useStatusCache = status != null && type == null;

            String cacheKey = null;
            if (useTypeCache) {
                cacheKey = CacheKeys.TRANSACTIONS_BY_TYPE_PREFIX + type;
            } else if (useStatusCache) {
                cacheKey = CacheKeys.TRANSACTIONS_BY_STATUS_PREFIX + status;
            }

            if (cacheKey != null) {
                transactionResponses = redisService.getList(cacheKey, TransactionResponse.class);
                if (transactionResponses == null) {
                    List<Transaction> transactions = transactionRepository.findAllWithFilters(
                            request.getUserId(), code, status, type, sourceAccount, destinationAccount, bankCode, null
                    );
                    transactionResponses = buildTransactionResponses(transactions);
                    redisService.setList(cacheKey, transactionResponses, 5, TimeUnit.MINUTES);
                }
            } else {
                List<Transaction> transactions = transactionRepository.findAllWithFilters(
                        request.getUserId(), code, status, type, sourceAccount, destinationAccount, bankCode, null
                );
                transactionResponses = buildTransactionResponses(transactions);
            }

            finalResults.addAll(transactionResponses);
        }

        int start = Math.min(skipCount, finalResults.size());
        int end = Math.min(start + maxResultCount, finalResults.size());

        List<TransactionResponse> paginatedResults = finalResults.subList(start, end);

        return BaseGetAllResponse.<TransactionResponse>builder()
                .data(paginatedResults)
                .totalRecords(finalResults.size())
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
        UserResponse user = getUserFromCacheOrFeign(transaction.getUserId());

        // XÓA CACHE
        evictUserTransactionCache(transaction.getUserId(), transaction.getType(), transaction.getStatus());

        return TransactionMapper.toResponse(transaction, user);

    }

    public TransactionResponse update(TransactionUpdateRequest request) {
        Transaction transaction = transactionRepository.findById(request.getId())
                .orElseThrow(() -> new AppException(ErrorCode.BAD_REQUEST, "Transaction not found"));

        if (request.getAmount() != null && request.getAmount().compareTo(BigDecimal.ZERO) > 0) {
            transaction.setAmount(request.getAmount());
        }

        if (request.getStatus() != null) {
            transaction.setStatus(request.getStatus());
        }

        transaction.setUpdatedAt(LocalDateTime.now());
        transactionRepository.save(transaction);
        UserResponse user = getUserFromCacheOrFeign(transaction.getUserId());


        // XÓA CACHE
        evictUserTransactionCache(transaction.getUserId(), transaction.getType(), transaction.getStatus());


        return TransactionMapper.toResponse(transaction, user);
    }

    public void delete(UUID id) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.BAD_REQUEST, "Transaction not found"));

        transactionRepository.softDeleteById(id);


        // XÓA CACHE
        evictUserTransactionCache(transaction.getUserId(), transaction.getType(), transaction.getStatus());
    }

    public TransactionResponse getById(UUID id) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.BAD_REQUEST, "Transaction not found"));
        // Lấy thông tin người dùng từ cache hoặc gọi Feign client
        UserResponse user = getUserFromCacheOrFeign(transaction.getUserId());
        // Chuyển đổi Transaction sang TransactionRespons
        return TransactionMapper.toResponse(transaction, user);
    }

    public TransactionResponse approve(UUID transactionId) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new AppException(ErrorCode.BAD_REQUEST, "Transaction not found"));

        if (transaction.getStatus() != TransactionStatusEnum.PENDING) {
            throw new AppException(ErrorCode.BAD_REQUEST, "Transaction is not in PENDING status");
        }

        transaction.setStatus(TransactionStatusEnum.SUCCESS);
        transaction.setUpdatedAt(LocalDateTime.now());
        transactionRepository.save(transaction);

        evictUserTransactionCache(transaction.getUserId(), transaction.getType(), transaction.getStatus());

        return toResponse(transaction);
    }

    public TransactionResponse reject(UUID transactionId) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new AppException(ErrorCode.BAD_REQUEST, "Transaction not found"));

        if (transaction.getStatus() != TransactionStatusEnum.PENDING) {
            throw new AppException(ErrorCode.BAD_REQUEST, "Transaction is not in PENDING status");
        }

        transaction.setStatus(TransactionStatusEnum.FAILED);
        transaction.setUpdatedAt(LocalDateTime.now());
        transactionRepository.save(transaction);

        evictUserTransactionCache(transaction.getUserId(), transaction.getType(), transaction.getStatus());

        return toResponse(transaction);
    }

    private void evictUserTransactionCache(UUID userId, TransactionTypeEnum type, TransactionStatusEnum status) {
        redisService.delete(CacheKeys.TRANSACTIONS_PREFIX + userId);
        if (type != null) {
            redisService.delete(CacheKeys.TRANSACTIONS_BY_TYPE_PREFIX + type);
        }
        if (status != null) {
            redisService.delete(CacheKeys.TRANSACTIONS_BY_STATUS_PREFIX + status);
        }
    }

    private TransactionResponse toResponse(Transaction tx) {
        UserResponse user = getUserFromCacheOrFeign(tx.getUserId());
        return TransactionMapper.toResponse(tx, user);
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

    private String generateCode() {
        return String.format("%08d", new Random().nextInt(100_000_000));
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
