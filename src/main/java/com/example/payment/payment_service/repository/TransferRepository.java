package com.example.payment.payment_service.repository;

import com.example.payment.payment_service.entity.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for Transfer entity.
 */
@Repository
public interface TransferRepository extends JpaRepository<Transfer, Long> {
    List<Transfer> findBySourceAccountIdOrDestinationAccountId(
            Long sourceAccountId,
            Long destinationAccountId
    );
}
