package com.example.complain_service.repository;

import com.example.complain_service.entity.Complain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ComplainRepository extends JpaRepository<Complain, UUID> {
    @Modifying
    @Query("update Complain c set c.isDeleted = true where c.id = :id")
    void softDeleteById(@Param("id") UUID id);

    @Query("""
        select c from Complain c
        where c.isDeleted = false
        and (:transactionId is null or c.transactionId = :transactionId)
        and (:resolverId is null or c.resolverId = :resolverId)
        and (:status is null or c.status = :status)
        and (
            :keyword is null or (
                c.content like %:keyword%
                or c.resolvingNote like %:keyword%
            )
        )
        order by c.updatedAt desc
        """)
    List<Complain> findAllByFilters(
            @Param("transactionId") UUID transactionId,
            @Param("resolverId") UUID resolverId,
            @Param("status") String status,
            @Param("keyword") String keyword
    );

    @Query("""
    select c from Complain c
    where c.isDeleted = false
    and c.status = 'PENDING'
    and c.resolverId is null
    and (:transactionId is null or c.transactionId = :transactionId)
    and (
        :keyword is null or (
            c.content like %:keyword%
            or c.resolvingNote like %:keyword%
        )
    )
    order by c.updatedAt desc

    """)
    List<Complain> findAllUnresolved(
            @Param("transactionId") UUID transactionId,
            @Param("keyword") String keyword
    );

}
