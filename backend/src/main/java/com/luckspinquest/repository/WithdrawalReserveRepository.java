package com.luckspinquest.repository;

import com.luckspinquest.entity.WithdrawalReserve;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WithdrawalReserveRepository
        extends JpaRepository<WithdrawalReserve, Long> {

    Optional<WithdrawalReserve> findByWithdrawalWithdrawalId(
        Long withdrawalId
    );

    List<WithdrawalReserve> findByWalletWalletIdOrderByCreatedAtDesc(
        Long walletId
    );

    List<WithdrawalReserve> findByUserUserIdOrderByCreatedAtDesc(
        Long userId
    );

    List<WithdrawalReserve> findByUserUserIdAndReserveStatusOrderByCreatedAtDesc(
        Long userId,
        String reserveStatus
    );
}
