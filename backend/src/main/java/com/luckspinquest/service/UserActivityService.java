package com.luckspinquest.service;

import com.luckspinquest.dto.user.UserActivityResponse;
import com.luckspinquest.entity.Redemption;
import com.luckspinquest.entity.Referral;
import com.luckspinquest.entity.SpinResult;
import com.luckspinquest.entity.User;
import com.luckspinquest.entity.UserQuest;
import com.luckspinquest.entity.WalletTransaction;
import com.luckspinquest.entity.Notification;
import com.luckspinquest.repository.RedemptionRepository;
import com.luckspinquest.repository.ReferralRepository;
import com.luckspinquest.repository.SpinResultRepository;
import com.luckspinquest.repository.UserQuestRepository;
import com.luckspinquest.repository.UserRepository;
import com.luckspinquest.repository.WalletTransactionRepository;
import com.luckspinquest.repository.NotificationRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class UserActivityService {

    private final UserRepository userRepository;
    private final SpinResultRepository spinResultRepository;
    private final UserQuestRepository userQuestRepository;
    private final WalletTransactionRepository walletTransactionRepository;
    private final RedemptionRepository redemptionRepository;
    private final ReferralRepository referralRepository;
    private final NotificationRepository notificationRepository;

    public UserActivityService(
            UserRepository userRepository,
            SpinResultRepository spinResultRepository,
            UserQuestRepository userQuestRepository,
            WalletTransactionRepository walletTransactionRepository,
            RedemptionRepository redemptionRepository,
            ReferralRepository referralRepository,
            NotificationRepository notificationRepository
    ) {
        this.userRepository = userRepository;
        this.spinResultRepository = spinResultRepository;
        this.userQuestRepository = userQuestRepository;
        this.walletTransactionRepository = walletTransactionRepository;
        this.redemptionRepository = redemptionRepository;
        this.referralRepository = referralRepository;
        this.notificationRepository = notificationRepository;
    }

    public List<UserActivityResponse> getActivity() {

        User user = getAuthenticatedUser();
        Long userId = user.getUserId();

        List<UserActivityResponse> activities = new ArrayList<>();

        for (SpinResult result :
                spinResultRepository
                        .findByUserUserIdOrderByCreatedAtDesc(userId)) {

            activities.add(
                    new UserActivityResponse(
                            "SPIN_RESULT",
                            result.getSpinResultId(),
                            "Spin result: " + result.getResultType(),
                            result.getCreatedAt()
                    )
            );
        }

        for (UserQuest userQuest :
                userQuestRepository
                        .findByUserUserIdOrderByStartedAtDesc(userId)) {

            activities.add(
                    new UserActivityResponse(
                            "QUEST",
                            userQuest.getUserQuestId(),
                            "Quest status: " + userQuest.getQuestStatus(),
                            userQuest.getCreatedAt()
                    )
            );
        }

        for (WalletTransaction transaction :
                walletTransactionRepository
                        .findByUserUserIdOrderByCreatedAtDesc(userId)) {

            activities.add(
                    new UserActivityResponse(
                            "WALLET_TRANSACTION",
                            transaction.getWalletTransactionId(),
                            transaction.getDescription(),
                            transaction.getCreatedAt()
                    )
            );
        }

        for (Redemption redemption :
                redemptionRepository
                        .findByUserUserIdOrderByRequestedAtDesc(userId)) {

            activities.add(
                    new UserActivityResponse(
                            "REDEMPTION",
                            redemption.getRedemptionId(),
                            "Redemption status: "
                                    + redemption.getRedemptionStatus(),
                            redemption.getCreatedAt()
                    )
            );
        }

        for (Referral referral :
                referralRepository
                        .findByReferrerUserUserIdOrderByCreatedAtDesc(userId)) {

            activities.add(
                    new UserActivityResponse(
                            "REFERRAL",
                            referral.getReferralId(),
                            "Referral status: "
                                    + referral.getReferralStatus(),
                            referral.getCreatedAt()
                    )
            );
        }

        for (Notification notification :
                notificationRepository
                        .findByUserUserIdOrderByCreatedAtDesc(userId)) {

            activities.add(
                    new UserActivityResponse(
                            "NOTIFICATION",
                            notification.getNotificationId(),
                            notification.getTitle(),
                            notification.getCreatedAt()
                    )
            );
        }

        activities.sort(
                Comparator.comparing(
                        UserActivityResponse::getTimestamp,
                        Comparator.nullsLast(Comparator.reverseOrder())
                )
        );

        return activities;
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
