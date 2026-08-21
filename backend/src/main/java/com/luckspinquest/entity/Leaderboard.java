package com.luckspinquest.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(
    name = "leaderboards",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "leaderboards_leaderboard_code_key",
            columnNames = "leaderboard_code"
        )
    }
)
public class Leaderboard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "leaderboard_id")
    private Long leaderboardId;

    @Column(
        name = "leaderboard_code",
        nullable = false,
        unique = true,
        length = 50
    )
    private String leaderboardCode;

    @Column(name = "leaderboard_name", nullable = false, length = 100)
    private String leaderboardName;

    @Column(name = "leaderboard_type", nullable = false, length = 50)
    private String leaderboardType;

    @Column(name = "start_at", nullable = false)
    private LocalDateTime startAt;

    @Column(name = "end_at", nullable = false)
    private LocalDateTime endAt;

    @Column(name = "reward_pool", nullable = false)
    private Long rewardPool = 0L;

    @Column(name = "leaderboard_status", nullable = false, length = 20)
    private String leaderboardStatus = "ACTIVE";

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();

        if (createdAt == null) {
            createdAt = now;
        }

        if (updatedAt == null) {
            updatedAt = now;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public Long getLeaderboardId() {
        return leaderboardId;
    }

    public void setLeaderboardId(Long leaderboardId) {
        this.leaderboardId = leaderboardId;
    }

    public String getLeaderboardCode() {
        return leaderboardCode;
    }

    public void setLeaderboardCode(String leaderboardCode) {
        this.leaderboardCode = leaderboardCode;
    }

    public String getLeaderboardName() {
        return leaderboardName;
    }

    public void setLeaderboardName(String leaderboardName) {
        this.leaderboardName = leaderboardName;
    }

    public String getLeaderboardType() {
        return leaderboardType;
    }

    public void setLeaderboardType(String leaderboardType) {
        this.leaderboardType = leaderboardType;
    }

    public LocalDateTime getStartAt() {
        return startAt;
    }

    public void setStartAt(LocalDateTime startAt) {
        this.startAt = startAt;
    }

    public LocalDateTime getEndAt() {
        return endAt;
    }

    public void setEndAt(LocalDateTime endAt) {
        this.endAt = endAt;
    }

    public Long getRewardPool() {
        return rewardPool;
    }

    public void setRewardPool(Long rewardPool) {
        this.rewardPool = rewardPool;
    }

    public String getLeaderboardStatus() {
        return leaderboardStatus;
    }

    public void setLeaderboardStatus(String leaderboardStatus) {
        this.leaderboardStatus = leaderboardStatus;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
