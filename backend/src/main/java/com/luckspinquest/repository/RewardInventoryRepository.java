package com.luckspinquest.repository;

import com.luckspinquest.entity.RewardInventory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RewardInventoryRepository
        extends JpaRepository<RewardInventory, Long> {

    List<RewardInventory> findByRewardRewardId(Long rewardId);

    List<RewardInventory> findByRewardRewardIdAndInventoryStatus(
        Long rewardId,
        String inventoryStatus
    );

    List<RewardInventory> findByInventoryStatus(
        String inventoryStatus
    );
}
