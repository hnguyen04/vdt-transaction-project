package com.example.complain_service.dto.transaction;

import com.example.complain_service.common.constants.TransactionStatusEnum;
import com.example.complain_service.common.constants.TransactionTypeEnum;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionResponse {
    private UUID id;
    private UUID userId;
    private String code;
    private LocalDateTime commitTime;
    private BigDecimal amount;
    @Enumerated(EnumType.STRING)
    private TransactionStatusEnum status;
    @Enumerated(EnumType.STRING)
    private TransactionTypeEnum type;
    private String sourceAccount;
    private String destinationAccount;
    private String bankCode;
    private String userName;
    private String fullName;
    private String phoneNumber;
    private String cmnd;
    private String userCode;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
