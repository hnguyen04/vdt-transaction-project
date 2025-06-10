package com.example.complain_service.dto.complain;

import com.example.complain_service.common.constants.ComplainStatusEnum;
import com.example.complain_service.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ComplainResponse {
    private UUID userId;
    private String userName;
    private String userFullName;
    private String content;
    private UUID resolverId;
    private String resolverName;
    private String resolverFullName;
    private String resolverPhoneNumber;
    private ComplainStatusEnum status;
    private String resolvingNote;
    private LocalDateTime timeSubmit;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
