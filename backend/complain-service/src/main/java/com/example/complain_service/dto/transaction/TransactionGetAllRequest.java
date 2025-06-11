package com.example.complain_service.dto.transaction;

import com.example.complain_service.common.constants.TransactionStatusEnum;
import com.example.complain_service.common.constants.TransactionTypeEnum;
import com.example.complain_service.common.dto.request.BaseGetAllRequest;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TransactionGetAllRequest extends BaseGetAllRequest {
    private UUID userId;
    private String code;
    @Enumerated(EnumType.STRING)
    private TransactionStatusEnum status;
    @Enumerated(EnumType.STRING)
    private TransactionTypeEnum type;
    private String sourceAccount;
    private String destinationAccount;
    private String bankCode;
    private String userName;
    private String phoneNumber;
    private String cmnd;
    private String userCode;
}
