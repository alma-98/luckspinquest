package com.luckspinquest.repository;

import com.luckspinquest.entity.WithdrawalTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WithdrawalTransactionRepository
        extends JpaRepository<WithdrawalTransaction, Long> {

    List<WithdrawalTransaction> findByUserUserIdOrderByCreatedAtDesc(
        Long userId
    );

    List<WithdrawalTransaction> findByWalletWalletIdOrderByCreatedAtDesc(
        Long walletId
    );

    List<WithdrawalTransaction> findByUserUserIdAndWithdrawalStatusOrderByCreatedAtDesc(
        Long userId,
        String withdrawalStatus
    );

    List<WithdrawalTransaction> findByPaymentAccountPaymentAccountIdOrderByCreatedAtDesc(
        Long paymentAccountId
    );

    Optional<WithdrawalTransaction> findByWithdrawalReference(
        String withdrawalReference
    );

    boolean existsByWithdrawalReference(
        String withdrawalReference
    );
}
