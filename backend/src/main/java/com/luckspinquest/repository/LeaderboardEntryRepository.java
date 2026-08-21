package com.luckspinquest.repository;

import com.luckspinquest.entity.LeaderboardEntry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LeaderboardEntryRepository
        extends JpaRepository<LeaderboardEntry, Long> {

    List<LeaderboardEntry> findByLeaderboardLeaderboardIdOrderByRankPositionAsc(
        Long leaderboardId
    );

    List<LeaderboardEntry> findByLeaderboardLeaderboardIdOrderByScoreDesc(
        Long leaderboardId
    );

    List<LeaderboardEntry> findByUserUserIdOrderByUpdatedAtDesc(
        Long userId
    );

    List<LeaderboardEntry> findByLeaderboardLeaderboardIdAndUserUserId(
        Long leaderboardId,
        Long userId
    );

    List<LeaderboardEntry> findByLeaderboardLeaderboardIdAndRewardStatus(
        Long leaderboardId,
        String rewardStatus
    );

    List<LeaderboardEntry> findByUserUserIdAndRewardStatus(
        Long userId,
        String rewardStatus
    );
}
