package com.example.auth_service.repository;

import com.example.auth_service.common.constants.RoleEnum;
import com.example.auth_service.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByUserName(String userName);
    boolean existsByUserName(String username);
    boolean existsByCmnd(String cmnd);
    boolean existsByPhoneNumber(String phoneNumber);

    @Modifying
    @Query("update User u set u.isDeleted = true where u.id = :id")
    void softDeleteById(@Param("id") UUID id);

    @Query("""
        SELECT u FROM User u
        WHERE (:userName IS NULL OR u.userName LIKE %:userName%)
        AND (:fullName IS NULL OR u.fullName LIKE %:fullName%)
        AND (:phoneNumber IS NULL OR u.phoneNumber LIKE %:phoneNumber%)
        AND (:cmnd IS NULL OR u.cmnd LIKE %:cmnd%)
        AND (:role IS NULL OR u.role = :role)
        AND (
            :keyword IS NULL OR (
                u.userName LIKE %:keyword%
                OR u.fullName LIKE %:keyword%
                OR u.phoneNumber LIKE %:keyword%
                OR u.cmnd LIKE %:keyword%
                OR u.code LIKE %:keyword%
                )
            )
        ORDER BY u.updatedAt DESC

        """
    )
    List<User> findAllByFilters(@Param("userName") String userName,
                                @Param("fullName") String fullName,
                                @Param("phoneNumber") String phoneNumber,
                                @Param("cmnd") String cmnd,
                                @Param("role") RoleEnum role,
                                @Param("keyword") String keyword,
                                @Param("code") String code);
}
