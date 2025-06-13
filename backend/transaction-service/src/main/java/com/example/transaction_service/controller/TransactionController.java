package com.example.transaction_service.controller;



import com.example.transaction_service.common.constants.TransactionStatusEnum;
import com.example.transaction_service.common.constants.TransactionTypeEnum;
import com.example.transaction_service.common.dto.response.ApiResponse;
import com.example.transaction_service.common.dto.response.BaseGetAllResponse;
import com.example.transaction_service.dto.transaction.*;
import com.example.transaction_service.service.transaction.TransactionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class TransactionController {

    TransactionService transactionService;

    @PostMapping("/Create")
    public ApiResponse<TransactionResponse> createTransaction(@RequestBody TransactionCreateRequest request) {
        return ApiResponse.<TransactionResponse>builder()
                .result(transactionService.create(request))
                .build();
    }

    @PutMapping("/Update")
    public ApiResponse<TransactionResponse> updateTransaction(@RequestBody TransactionUpdateRequest request) {
        return ApiResponse.<TransactionResponse>builder()
                .result(transactionService.update(request))
                .build();
    }

    @GetMapping("/GetAll")
    public ApiResponse<BaseGetAllResponse<TransactionResponse>> getAllTransactions(
            @RequestParam(value = "skipCount", defaultValue = "0") int skipCount,
            @RequestParam(value = "maxResultCount", defaultValue = "10") int maxResultCount,
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "userId", required = false) UUID userId,
            @RequestParam(value = "code", required = false) String code,
            @RequestParam(value = "status", required = false) TransactionStatusEnum status,
            @RequestParam(value = "type", required = false) TransactionTypeEnum type,
            @RequestParam(value = "sourceAccount", required = false) String sourceAccount,
            @RequestParam(value = "destinationAccount", required = false) String destinationAccount,
            @RequestParam(value = "bankCode", required = false) String bankCode,
            @RequestParam(value = "userName", required = false) String userName,
            @RequestParam(value = "phoneNumber", required = false) String phoneNumber,
            @RequestParam(value = "cmnd", required = false) String cmnd,
            @RequestParam(value = "userCode", required = false) String userCode,
            @RequestParam(value = "userFullName", required = false) String userFullName
    ) {
        TransactionGetAllRequest request = TransactionGetAllRequest.builder()
                .userId(userId)
                .code(code)
                .status(status)
                .type(type)
                .sourceAccount(sourceAccount)
                .destinationAccount(destinationAccount)
                .bankCode(bankCode)
                .userName(userName)
                .phoneNumber(phoneNumber)
                .cmnd(cmnd)
                .userCode(userCode)
                .userFullName(userFullName)
                .build();
        request.setSkipCount(skipCount);
        request.setMaxResultCount(maxResultCount);
        request.setKeyword(keyword);

        return ApiResponse.<BaseGetAllResponse<TransactionResponse>>builder()
                .result(transactionService.getAll(request))
                .build();
    }

    @GetMapping("/GetById")
    public ApiResponse<TransactionResponse> getTransactionById(@RequestParam UUID id) {
        return ApiResponse.<TransactionResponse>builder()
                .result(transactionService.getById(id))
                .build();
    }

    @DeleteMapping("/Delete")
    public ApiResponse<String> deleteTransaction(@RequestParam UUID id) {
        transactionService.delete(id);
        return ApiResponse.<String>builder()
                .result("Transaction deleted successfully")
                .build();
    }

    @PostMapping("/Approve")
    public ApiResponse<TransactionResponse> approveTransaction(@RequestParam UUID id) {
        return ApiResponse.<TransactionResponse>builder()
                .result(transactionService.approve(id))
                .build();
    }

    @PostMapping("/Reject")
    public ApiResponse<TransactionResponse> rejectTransaction(@RequestParam UUID id) {
        return ApiResponse.<TransactionResponse>builder()
                .result(transactionService.reject(id))
                .build();
    }


}