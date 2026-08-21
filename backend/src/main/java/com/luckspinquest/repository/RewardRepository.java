package com.luckspinquest.repository;

import com.luckspinquest.entity.Reward;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RewardRepository
        extends JpaRepository<Reward, Long> {

    Optional<Reward> findByRewardCode(String rewardCode);

    List<Reward> findByRewardStatus(String rewardStatus);

    List<Reward> findByRewardStatusOrderByRewardNameAsc(
        String rewardStatus
    );

    List<Reward> findByRewardType(String rewardType);

    boolean existsByRewardCode(String rewardCode);
}
