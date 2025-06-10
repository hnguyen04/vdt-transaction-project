package com.example.auth_service.entity;

import com.example.auth_service.common.constants.RoleEnum;
import com.example.auth_service.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.SQLRestriction;


@Entity
@SQLRestriction("is_deleted = false")
@Table(name = "users")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseEntity{

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "user_name", nullable = false, unique = true)
    private String userName;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "code", length = 8, nullable = false, unique = true)
    private String code; // mã 8 số tự gen

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Column(name = "cmnd", nullable = false)
    private String cmnd;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private RoleEnum role;

    @Column(name = "password", nullable = false)
    private String password;
}
