package com.luckspinquest.service;

import com.luckspinquest.entity.User;
import com.luckspinquest.entity.Wallet;
import com.luckspinquest.repository.NotificationRepository;
import com.luckspinquest.repository.RedemptionRepository;
import com.luckspinquest.repository.ReferralRepository;
import com.luckspinquest.repository.SpinResultRepository;
import com.luckspinquest.repository.SpinSessionRepository;
import com.luckspinquest.repository.UserRepository;
import com.luckspinquest.repository.WalletRepository;
import com.luckspinquest.repository.WalletTransactionRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.Map;

@Service
@Transactional(readOnly = true)
public class UserStatisticsService {

    private final UserRepository userRepository;
    private final WalletRepository walletRepository;
    private final WalletTransactionRepository walletTransactionRepository;
    private final SpinSessionRepository spinSessionRepository;
    private final SpinResultRepository spinResultRepository;
    private final ReferralRepository referralRepository;
    private final RedemptionRepository redemptionRepository;
    private final NotificationRepository notificationRepository;

    public UserStatisticsService(
            UserRepository userRepository,
            WalletRepository walletRepository,
            WalletTransactionRepository walletTransactionRepository,
            SpinSessionRepository spinSessionRepository,
            SpinResultRepository spinResultRepository,
            ReferralRepository referralRepository,
            RedemptionRepository redemptionRepository,
            NotificationRepository notificationRepository
    ) {
        this.userRepository = userRepository;
        this.walletRepository = walletRepository;
        this.walletTransactionRepository = walletTransactionRepository;
        this.spinSessionRepository = spinSessionRepository;
        this.spinResultRepository = spinResultRepository;
        this.referralRepository = referralRepository;
        this.redemptionRepository = redemptionRepository;
        this.notificationRepository = notificationRepository;
    }

    public Map<String, Object> getStatistics() {
        User user = getAuthenticatedUser();
        Long userId = user.getUserId();

        Map<String, Object> response = new LinkedHashMap<>();

        response.put("userId", userId);
        response.put("username", user.getUserUsername());

        Wallet wallet = walletRepository
                .findByUserUserId(userId)
                .orElse(null);

        if (wallet != null) {
            response.put("walletId", wallet.getWalletId());
            response.put("balance", wallet.getWalletBalance());
            response.put("lockedBalance", wallet.getWalletLockedBalance());
            response.put(
                    "availableBalance",
                    wallet.getWalletAvailableBalance()
            );
            response.put("currency", wallet.getWalletCurrency());
        } else {
            response.put("walletId", null);
            response.put("balance", 0);
            response.put("lockedBalance", 0);
            response.put("availableBalance", 0);
            response.put("currency", null);
        }

        response.put(
                "walletTransactionCount",
                wallet != null
                        ? walletTransactionRepository
                                .countByWalletWalletId(wallet.getWalletId())
                        : 0
        );

        response.put(
                "spinSessionCount",
                spinSessionRepository.countByUserUserId(userId)
        );

        response.put(
                "spinResultCount",
                spinResultRepository.countByUserUserId(userId)
        );

        response.put(
                "referralCount",
                referralRepository.countByReferrerUserUserId(userId)
        );

        response.put(
                "redemptionCount",
                redemptionRepository.countByUserUserId(userId)
        );

        response.put(
                "unreadNotificationCount",
                notificationRepository
                        .countByUserUserIdAndIsReadFalse(userId)
        );

        return response;
    }

    private User getAuthenticatedUser() {
        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        if (authentication == null
                || !authentication.isAuthenticated()
                || authentication.getName() == null
                || "anonymousUser".equals(authentication.getName())) {

            throw new IllegalStateException(
                    "User is not authenticated"
            );
        }

        return userRepository
                .findByUserUsername(authentication.getName())
                .orElseThrow(() ->
                        new IllegalStateException(
                                "Authenticated user not found"
                        )
                );
    }
}
