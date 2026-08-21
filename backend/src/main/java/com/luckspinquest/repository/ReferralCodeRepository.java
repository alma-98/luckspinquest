package com.luckspinquest.repository;

import com.luckspinquest.entity.ReferralCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReferralCodeRepository
        extends JpaRepository<ReferralCode, Long> {

    Optional<ReferralCode> findByReferralCode(
        String referralCode
    );

    List<ReferralCode> findByUserUserId(
        Long userId
    );

    List<ReferralCode> findByReferralStatus(
        String referralStatus
    );

    List<ReferralCode> findByUserUserIdAndReferralStatus(
        Long userId,
        String referralStatus
    );

    boolean existsByReferralCode(
        String referralCode
    );
}
