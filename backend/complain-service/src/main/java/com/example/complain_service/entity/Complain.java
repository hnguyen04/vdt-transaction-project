package com.example.complain_service.entity;

import com.example.complain_service.common.entity.BaseEntity;
import com.example.complain_service.common.constants.ComplainStatusEnum;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "complains")
@SQLRestriction("is_deleted = false")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Complain extends BaseEntity {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "user_id", nullable = false)
    private UUID userId;

    @Column(name = "content", columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(name = "resolver_id")
    private UUID resolverId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ComplainStatusEnum status;

    @Column(name = "resolving_note", columnDefinition = "TEXT")
    private String resolvingNote;

    @Column(name = "time_submit", nullable = false)
    private LocalDateTime timeSubmit;
}
