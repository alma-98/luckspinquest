package com.luckspinquest.service;

import com.luckspinquest.dto.wallet.WalletResponse;
import com.luckspinquest.entity.User;
import com.luckspinquest.entity.Wallet;
import com.luckspinquest.entity.WalletTransaction;
import com.luckspinquest.repository.UserRepository;
import com.luckspinquest.repository.WalletRepository;
import com.luckspinquest.repository.WalletTransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
public class WalletService {

    private final UserRepository userRepository;
    private final WalletRepository walletRepository;
    private final WalletTransactionRepository walletTransactionRepository;

    public WalletService(
            UserRepository userRepository,
            WalletRepository walletRepository,
            WalletTransactionRepository walletTransactionRepository
    ) {
        this.userRepository = userRepository;
        this.walletRepository = walletRepository;
        this.walletTransactionRepository = walletTransactionRepository;
    }

    public WalletResponse getWallet(String username) {

        User user = getUser(username);

        Wallet wallet = walletRepository
                .findByUserUserId(user.getUserId())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Wallet not found"
                        )
                );

        return toResponse(wallet);
    }

    public Map<String, Object> getBalance(String username) {

        User user = getUser(username);

        Wallet wallet = walletRepository
                .findByUserUserId(user.getUserId())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Wallet not found"
                        )
                );

        Map<String, Object> response = new LinkedHashMap<>();

        response.put("walletId", wallet.getWalletId());
        response.put("userId", user.getUserId());
        response.put("balance", wallet.getWalletBalance());
        response.put("lockedBalance", wallet.getWalletLockedBalance());
        response.put(
                "availableBalance",
                wallet.getWalletAvailableBalance()
        );
        response.put("currency", wallet.getWalletCurrency());

        return response;
    }

    public List<WalletTransaction> getTransactions(String username) {

        User user = getUser(username);

        Wallet wallet = walletRepository
                .findByUserUserId(user.getUserId())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Wallet not found"
                        )
                );

        return walletTransactionRepository
                .findByWalletWalletIdOrderByCreatedAtDesc(
                        wallet.getWalletId()
                );
    }

    public List<WalletTransaction> getAllTransactions() {

        return walletTransactionRepository
                .findAll(
                        org.springframework.data.domain.Sort
                                .by(
                                        org.springframework.data.domain.Sort.Direction.DESC,
                                        "createdAt"
                                )
                );
    }

    public WalletTransaction getTransactionById(Long transactionId) {

        return walletTransactionRepository
                .findById(transactionId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Transaction not found"
                        )
                );
    }

    public WalletTransaction getTransaction(
            String username,
            Long transactionId
    ) {

        User user = getUser(username);

        Wallet wallet = walletRepository
                .findByUserUserId(user.getUserId())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Wallet not found"
                        )
                );

        List<WalletTransaction> transactions =
                walletTransactionRepository
                        .findByWalletWalletIdOrderByCreatedAtDesc(
                                wallet.getWalletId()
                        );

        return transactions.stream()
                .filter(transaction ->
                        transaction.getWalletTransactionId()
                                .equals(transactionId)
                )
                .findFirst()
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Transaction not found"
                        )
                );
    }

    @Transactional
    public WalletTransaction adjustWallet(Long userId, Long amount) {

        if (amount == null || amount <= 0) {
            throw new IllegalArgumentException(
                    "Adjustment amount must be greater than zero"
            );
        }

        User user = userRepository
                .findById(userId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "User not found"
                        )
                );

        Wallet wallet = walletRepository
                .findByUserUserId(userId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Wallet not found"
                        )
                );

        Long balanceBefore = wallet.getWalletBalance();
        Long balanceAfter = balanceBefore + amount;

        wallet.setWalletBalance(balanceAfter);
        wallet.setWalletAvailableBalance(
                wallet.getWalletAvailableBalance() + amount
        );

        walletRepository.save(wallet);

        WalletTransaction transaction = new WalletTransaction();

        transaction.setTransactionReference(
                "ADMIN-ADJUST-" + System.currentTimeMillis()
        );
        transaction.setWallet(wallet);
        transaction.setUser(user);
        transaction.setTransactionType("ADMIN_ADJUSTMENT");
        transaction.setTransactionDirection("CREDIT");
        transaction.setAmount(amount);
        transaction.setBalanceBefore(balanceBefore);
        transaction.setBalanceAfter(balanceAfter);
        transaction.setReferenceType("ADMIN");
        transaction.setReferenceId(userId);
        transaction.setDescription("Manual wallet adjustment by admin");
        transaction.setTransactionStatus("SUCCESS");

        return walletTransactionRepository.save(transaction);
    }

    public Map<String, Object> getSummary(String username) {

        User user = getUser(username);

        Wallet wallet = walletRepository
                .findByUserUserId(user.getUserId())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Wallet not found"
                        )
                );

        long transactionCount =
                walletTransactionRepository
                        .countByWalletWalletId(
                                wallet.getWalletId()
                        );

        Map<String, Object> response = new LinkedHashMap<>();

        response.put("walletId", wallet.getWalletId());
        response.put("userId", user.getUserId());
        response.put("balance", wallet.getWalletBalance());
        response.put(
                "lockedBalance",
                wallet.getWalletLockedBalance()
        );
        response.put(
                "availableBalance",
                wallet.getWalletAvailableBalance()
        );
        response.put("currency", wallet.getWalletCurrency());
        response.put("status", wallet.getWalletStatus());
        response.put("transactionCount", transactionCount);

        return response;
    }

    private User getUser(String username) {

        return userRepository
                .findByUserUsername(username)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "User not found"
                        )
                );
    }

    private WalletResponse toResponse(Wallet wallet) {

        return new WalletResponse(
                wallet.getWalletId(),
                wallet.getUser().getUserId(),
                wallet.getWalletBalance(),
                wallet.getWalletLockedBalance(),
                wallet.getWalletAvailableBalance(),
                wallet.getWalletCurrency(),
                wallet.getWalletStatus(),
                wallet.getCreatedAt(),
                wallet.getUpdatedAt()
        );
    }
}
