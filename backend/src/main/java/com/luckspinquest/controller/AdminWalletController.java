package com.luckspinquest.controller;

import com.luckspinquest.entity.WalletTransaction;
import com.luckspinquest.dto.wallet.WalletTransactionResponse;
import com.luckspinquest.service.WalletService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminWalletController {

    private final WalletService walletService;

    public AdminWalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @GetMapping("/wallets")
    public ResponseEntity<?> getWallets() {
        return ResponseEntity.ok(
                Map.of("message", "Admin wallet list")
        );
    }

    @GetMapping("/wallets/{userId}")
    public ResponseEntity<?> getUserWallet(
            @PathVariable Long userId) {

        return ResponseEntity.ok(
                Map.of(
                        "message", "User wallet",
                        "userId", userId
                )
        );
    }

    @PostMapping("/wallets/{userId}/adjust")
    public ResponseEntity<?> adjustWallet(
            @PathVariable Long userId,
            @RequestParam Long amount) {

        WalletTransaction transaction =
                walletService.adjustWallet(userId, amount);

        return ResponseEntity.ok(
                Map.of(
                        "message", "Wallet balance adjusted",
                        "userId", userId,
                        "transactionId",
                        transaction.getWalletTransactionId(),
                        "amount",
                        transaction.getAmount(),
                        "balanceBefore",
                        transaction.getBalanceBefore(),
                        "balanceAfter",
                        transaction.getBalanceAfter()
                )
        );
    }

    @GetMapping("/wallet-transactions")
    public ResponseEntity<?> getWalletTransactions() {

        List<WalletTransactionResponse> transactions =
                walletService.getAllTransactions()
                        .stream()
                        .map(WalletTransactionResponse::from)
                        .toList();

        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/wallet-transactions/{id}")
    public ResponseEntity<?> getWalletTransaction(
            @PathVariable Long id) {

        WalletTransaction transaction =
                walletService.getTransactionById(id);

        return ResponseEntity.ok(
                WalletTransactionResponse.from(transaction)
        );
    }
}
