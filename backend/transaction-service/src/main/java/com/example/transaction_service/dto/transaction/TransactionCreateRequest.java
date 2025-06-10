package com.example.transaction_service.dto.transaction;

import com.example.transaction_service.common.constants.TransactionStatusEnum;
import com.example.transaction_service.common.constants.TransactionTypeEnum;
import com.example.transaction_service.common.dto.request.BaseGetAllRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionCreateRequest{
    private UUID userId;
    private LocalDateTime commitTime;
    private BigDecimal amount;
    @Enumerated(EnumType.STRING)
    private TransactionStatusEnum status;
    @Enumerated(EnumType.STRING)
    private TransactionTypeEnum type;
    private String sourceAccount;
    private String destinationAccount;
    private String bankCode;
}
