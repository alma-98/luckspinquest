package com.luckspinquest.controller;

import com.luckspinquest.dto.wallet.WalletResponse;
import com.luckspinquest.dto.wallet.WalletTransactionResponse;
import com.luckspinquest.entity.WalletTransaction;
import com.luckspinquest.service.WalletService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/wallet")
public class WalletController {

    private final WalletService walletService;

    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @GetMapping
    public ResponseEntity<?> getWallet() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        if (!isAuthenticated(authentication)) {
            return ResponseEntity.status(401).body(
                    Map.of("message", "Authentication required")
            );
        }

        WalletResponse response =
                walletService.getWallet(
                        authentication.getName()
                );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/balance")
    public ResponseEntity<?> getBalance() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        if (!isAuthenticated(authentication)) {
            return ResponseEntity.status(401).body(
                    Map.of("message", "Authentication required")
            );
        }

        return ResponseEntity.ok(
                walletService.getBalance(
                        authentication.getName()
                )
        );
    }

    @GetMapping("/transactions")
    public ResponseEntity<?> getTransactions() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        if (!isAuthenticated(authentication)) {
            return ResponseEntity.status(401).body(
                    Map.of("message", "Authentication required")
            );
        }

        List<WalletTransactionResponse> transactions =
                walletService.getTransactions(
                                authentication.getName()
                        )
                        .stream()
                        .map(WalletTransactionResponse::from)
                        .toList();

        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/transactions/{id}")
    public ResponseEntity<?> getTransaction(
            @PathVariable Long id
    ) {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        if (!isAuthenticated(authentication)) {
            return ResponseEntity.status(401).body(
                    Map.of("message", "Authentication required")
            );
        }

        WalletTransaction transaction =
                walletService.getTransaction(
                        authentication.getName(),
                        id
                );

        return ResponseEntity.ok(
                WalletTransactionResponse.from(transaction)
        );
    }

    @GetMapping("/summary")
    public ResponseEntity<?> getSummary() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        if (!isAuthenticated(authentication)) {
            return ResponseEntity.status(401).body(
                    Map.of("message", "Authentication required")
            );
        }

        return ResponseEntity.ok(
                walletService.getSummary(
                        authentication.getName()
                )
        );
    }

    private boolean isAuthenticated(
            Authentication authentication
    ) {

        return authentication != null
                && authentication.isAuthenticated()
                && authentication.getName() != null
                && !"anonymousUser".equals(
                        authentication.getName()
                );
    }
}
