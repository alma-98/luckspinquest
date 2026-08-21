package com.luckspinquest.repository;

import com.luckspinquest.entity.Redemption;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RedemptionRepository
        extends JpaRepository<Redemption, Long> {

    Optional<Redemption> findByReferenceCode(
        String referenceCode
    );

    List<Redemption> findByUserUserIdOrderByRequestedAtDesc(
        Long userId
    );

    List<Redemption> findByRewardRewardIdOrderByRequestedAtDesc(
        Long rewardId
    );

    List<Redemption> findByUserUserIdAndRedemptionStatusOrderByRequestedAtDesc(
        Long userId,
        String redemptionStatus
    );

    List<Redemption> findByRedemptionStatusOrderByRequestedAtDesc(
        String redemptionStatus
    );

    boolean existsByReferenceCode(
        String referenceCode
    );
}
