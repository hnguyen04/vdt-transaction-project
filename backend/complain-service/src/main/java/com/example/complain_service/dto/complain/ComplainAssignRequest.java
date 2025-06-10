package com.example.complain_service.dto.complain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ComplainAssignRequest {
    private UUID id;
    private UUID resolverId;
}
