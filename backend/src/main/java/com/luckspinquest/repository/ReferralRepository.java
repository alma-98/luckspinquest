package com.luckspinquest.repository;

import com.luckspinquest.entity.Referral;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReferralRepository
        extends JpaRepository<Referral, Long> {

    long countByReferrerUserUserId(Long referrerUserId);

    Optional<Referral> findByReferredUserUserId(
        Long referredUserId
    );

    List<Referral> findByReferrerUserUserIdOrderByCreatedAtDesc(
        Long referrerUserId
    );

    List<Referral> findByReferralCodeReferralCodeId(
        Long referralCodeId
    );

    List<Referral> findByRewardRewardId(
        Long rewardId
    );

    List<Referral> findByReferralStatusOrderByCreatedAtDesc(
        String referralStatus
    );

    List<Referral> findByReferrerUserUserIdAndReferralStatusOrderByCreatedAtDesc(
        Long referrerUserId,
        String referralStatus
    );

    boolean existsByReferredUserUserId(
        Long referredUserId
    );
}
