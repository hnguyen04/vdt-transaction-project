package com.example.complain_service.dto.complain;

import com.example.complain_service.common.dto.request.BaseGetAllRequest;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ComplainGetAllRequest extends BaseGetAllRequest {
    private UUID resolverId;
    private String resolverFullName;
    private UUID transactionId;
    private String transactionCode;
    private UUID userId;
    private String userFullName;
    private String status;
}
