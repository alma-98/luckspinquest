package com.luckspinquest.repository;

import com.luckspinquest.entity.TopupTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TopupTransactionRepository
        extends JpaRepository<TopupTransaction, Long> {

    List<TopupTransaction> findByUserUserIdOrderByCreatedAtDesc(
        Long userId
    );

    List<TopupTransaction> findByUserUserIdAndTopupStatusOrderByCreatedAtDesc(
        Long userId,
        String topupStatus
    );

    List<TopupTransaction> findByPaymentAccountPaymentAccountIdOrderByCreatedAtDesc(
        Long paymentAccountId
    );

    Optional<TopupTransaction> findByPaymentReference(
        String paymentReference
    );
}
