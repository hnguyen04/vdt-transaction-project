package com.example.complain_service.client;

import com.example.complain_service.common.constants.RoleEnum;
import com.example.complain_service.common.constants.TransactionStatusEnum;
import com.example.complain_service.common.constants.TransactionTypeEnum;
import com.example.complain_service.common.dto.response.ApiResponse;
import com.example.complain_service.common.dto.response.BaseGetAllResponse;
import com.example.complain_service.dto.transaction.TransactionResponse;
import com.example.complain_service.dto.user.UserResponse;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collections;
import java.util.UUID;

@Component
public class TransactionClientFallBackFactory implements FallbackFactory<TransactionClient> {

    @Override
    public TransactionClient create(Throwable cause) {
        System.err.println("TransactionClient fallback triggered due to: " + cause.getMessage());
        return new TransactionClient() {
            @Override
            public ApiResponse<BaseGetAllResponse<TransactionResponse>> getAllTransactions(
                    int skipCount,
                    int maxResultCount,
                    String keyword,
                    UUID userId,
                    String code,
                    TransactionStatusEnum status,
                    TransactionTypeEnum type,
                    String sourceAccount,
                    String destinationAccount,
                    String bankCode,
                    String userName,
                    String phoneNumber,
                    String cmnd,
                    String userCode
            ) {
                BaseGetAllResponse<TransactionResponse> emptyResponse = BaseGetAllResponse.<TransactionResponse>builder()
                        .data(Collections.emptyList())
                        .totalRecords(0)
                        .build();

                return ApiResponse.<BaseGetAllResponse<TransactionResponse>>builder()
                        .success(false)
                        .code(503)
                        .message("Transaction service currently unavailable. Please try again later.")
                        .result(emptyResponse)
                        .build();
            }

            @Override
            public ApiResponse<TransactionResponse> getTransactionById(UUID id) {
                return ApiResponse.<TransactionResponse>builder()
                        .success(false)
                        .code(503)
                        .message("Transaction service currently unavailable. Please try again later.")
                        .build();
            }
        };
    }
}