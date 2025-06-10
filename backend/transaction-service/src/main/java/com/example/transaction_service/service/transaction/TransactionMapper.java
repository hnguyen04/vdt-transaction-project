package com.example.transaction_service.service.transaction;

import com.example.transaction_service.dto.transaction.TransactionResponse;
import com.example.transaction_service.dto.user.UserResponse;
import com.example.transaction_service.entity.Transaction;

public class TransactionMapper {
    public static TransactionResponse toResponse(Transaction tx, UserResponse user) {
        return TransactionResponse.builder()
                .id(tx.getId())
                .code(tx.getCode())
                .amount(tx.getAmount())
                .status(tx.getStatus())
                .type(tx.getType())
                .bankCode(tx.getBankCode())
                .userId(tx.getUserId())
                .userName(user.getUserName())
                .fullName(user.getFullName())
                .phoneNumber(user.getPhoneNumber())
                .cmnd(user.getCmnd())
                .userCode(user.getCode())
                .sourceAccount(tx.getSourceAccount())
                .destinationAccount(tx.getDestinationAccount())
                .createdAt(tx.getCreatedAt())
                .build();
    }


}