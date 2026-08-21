package com.luckspinquest.repository;

import com.luckspinquest.entity.Leaderboard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface LeaderboardRepository
        extends JpaRepository<Leaderboard, Long> {

    Optional<Leaderboard> findByLeaderboardCode(
        String leaderboardCode
    );

    List<Leaderboard> findByLeaderboardStatus(
        String leaderboardStatus
    );

    List<Leaderboard> findByLeaderboardStatusOrderByStartAtDesc(
        String leaderboardStatus
    );

    List<Leaderboard> findByLeaderboardType(
        String leaderboardType
    );

    List<Leaderboard> findByStartAtBetween(
        LocalDateTime startAt,
        LocalDateTime endAt
    );

    List<Leaderboard> findByStartAtLessThanEqualAndEndAtGreaterThanEqual(
        LocalDateTime startAt,
        LocalDateTime endAt
    );

    boolean existsByLeaderboardCode(
        String leaderboardCode
    );
}
