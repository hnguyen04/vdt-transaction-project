package com.example.transaction_service.repository;

import com.example.transaction_service.common.constants.TransactionStatusEnum;
import com.example.transaction_service.common.constants.TransactionTypeEnum;
import com.example.transaction_service.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, UUID> {

    // Tìm theo code
    Optional<Transaction> findByCode(String code);
    boolean existsByCode(String code);

    @Modifying
    @Query("update Transaction t set t.isDeleted = true where t.id = :id")
    void softDeleteById(@Param("id") UUID id);

    @Query("""
    SELECT t FROM Transaction t
    WHERE t.isDeleted = false
    AND (:userId IS NULL OR t.userId = :userId)
    AND (:code IS NULL OR t.code = :code)
    AND (:status IS NULL OR t.status = :status)
    AND (:type IS NULL OR t.type = :type)
    AND (:sourceAccount IS NULL OR t.sourceAccount = :sourceAccount)
    AND (:destinationAccount IS NULL OR t.destinationAccount = :destinationAccount)
    AND (:bankCode IS NULL OR t.bankCode = :bankCode)
    AND (
        :keyword IS NULL OR (
            t.code LIKE %:keyword% OR
            t.sourceAccount LIKE %:keyword% OR
            t.destinationAccount LIKE %:keyword% OR
            t.bankCode LIKE %:keyword%
        )
    )
""")
    List<Transaction> findAllWithFilters(
            @Param("userId") UUID userId,
            @Param("code") String code,
            @Param("status") TransactionStatusEnum status,
            @Param("type") TransactionTypeEnum type,
            @Param("sourceAccount") String sourceAccount,
            @Param("destinationAccount") String destinationAccount,
            @Param("bankCode") String bankCode,
            @Param("keyword") String keyword
    );

}
