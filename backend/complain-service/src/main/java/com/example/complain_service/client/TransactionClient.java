package com.example.complain_service.client;


import com.example.complain_service.common.constants.RoleEnum;
import com.example.complain_service.common.constants.TransactionStatusEnum;
import com.example.complain_service.common.constants.TransactionTypeEnum;
import com.example.complain_service.common.dto.response.ApiResponse;
import com.example.complain_service.common.dto.response.BaseGetAllResponse;
import com.example.complain_service.config.FeignClientConfiguration;
import com.example.complain_service.dto.transaction.TransactionResponse;
import com.example.complain_service.dto.user.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@FeignClient(
        name = "transaction-service",
        url = "${transaction.service.url}",
        fallbackFactory = TransactionClientFallBackFactory.class,
        configuration = FeignClientConfiguration.class

)
public interface TransactionClient {

    @GetMapping("/transactions/GetAll")
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
            @RequestParam(value = "userCode", required = false) String userCode
    );

    @GetMapping("/transactions/GetById")
    public ApiResponse<TransactionResponse> getTransactionById(@RequestParam UUID id);
}
