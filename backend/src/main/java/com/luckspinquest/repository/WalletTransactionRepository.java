package com.luckspinquest.repository;

import com.luckspinquest.entity.WalletTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WalletTransactionRepository
        extends JpaRepository<WalletTransaction, Long> {

    Optional<WalletTransaction> findByTransactionReference(
        String transactionReference
    );

    List<WalletTransaction> findByWalletWalletIdOrderByCreatedAtDesc(
        Long walletId
    );

    long countByWalletWalletId(Long walletId);

    List<WalletTransaction> findByUserUserIdOrderByCreatedAtDesc(
        Long userId
    );

    List<WalletTransaction> findByUserUserIdAndTransactionStatusOrderByCreatedAtDesc(
        Long userId,
        String transactionStatus
    );

    boolean existsByTransactionReference(String transactionReference);
}
