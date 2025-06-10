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
public class ComplainCreateRequest{
    private UUID userId;
    private String content;
}
