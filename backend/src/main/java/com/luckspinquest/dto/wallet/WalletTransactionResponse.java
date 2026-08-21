package com.luckspinquest.dto.wallet;

import com.luckspinquest.entity.WalletTransaction;

import java.time.LocalDateTime;

public record WalletTransactionResponse(
        Long walletTransactionId,
        String transactionReference,
        Long walletId,
        Long userId,
        Long amount,
        Long balanceBefore,
        Long balanceAfter,
        String transactionType,
        String transactionDirection,
        String referenceType,
        Long referenceId,
        String description,
        String transactionStatus,
        LocalDateTime createdAt
) {

    public static WalletTransactionResponse from(
            WalletTransaction transaction
    ) {
        return new WalletTransactionResponse(
                transaction.getWalletTransactionId(),
                transaction.getTransactionReference(),
                transaction.getWallet().getWalletId(),
                transaction.getUser().getUserId(),
                transaction.getAmount(),
                transaction.getBalanceBefore(),
                transaction.getBalanceAfter(),
                transaction.getTransactionType(),
                transaction.getTransactionDirection(),
                transaction.getReferenceType(),
                transaction.getReferenceId(),
                transaction.getDescription(),
                transaction.getTransactionStatus(),
                transaction.getCreatedAt()
        );
    }
}
