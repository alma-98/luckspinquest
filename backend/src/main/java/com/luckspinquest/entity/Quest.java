package com.luckspinquest.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(
    name = "quests",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "quests_quest_code_key",
            columnNames = "quest_code"
        )
    }
)
public class Quest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "quest_id")
    private Long questId;

    @Column(
        name = "quest_code",
        nullable = false,
        unique = true,
        length = 50
    )
    private String questCode;

    @Column(name = "quest_name", nullable = false, length = 100)
    private String questName;

    @Column(name = "quest_description", columnDefinition = "TEXT")
    private String questDescription;

    @Column(name = "quest_type", nullable = false, length = 50)
    private String questType;

    @Column(name = "target_value", nullable = false)
    private Long targetValue;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "reward_id",
        foreignKey = @ForeignKey(name = "fk_quests_reward")
    )
    private Reward reward;

    @Column(name = "coin_reward", nullable = false)
    private Long coinReward = 0L;

    @Column(name = "start_at")
    private LocalDateTime startAt;

    @Column(name = "end_at")
    private LocalDateTime endAt;

    @Column(name = "quest_status", nullable = false, length = 20)
    private String questStatus = "ACTIVE";

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

    public Long getQuestId() {
        return questId;
    }

    public void setQuestId(Long questId) {
        this.questId = questId;
    }

    public String getQuestCode() {
        return questCode;
    }

    public void setQuestCode(String questCode) {
        this.questCode = questCode;
    }

    public String getQuestName() {
        return questName;
    }

    public void setQuestName(String questName) {
        this.questName = questName;
    }

    public String getQuestDescription() {
        return questDescription;
    }

    public void setQuestDescription(String questDescription) {
        this.questDescription = questDescription;
    }

    public String getQuestType() {
        return questType;
    }

    public void setQuestType(String questType) {
        this.questType = questType;
    }

    public Long getTargetValue() {
        return targetValue;
    }

    public void setTargetValue(Long targetValue) {
        this.targetValue = targetValue;
    }

    public Reward getReward() {
        return reward;
    }

    public void setReward(Reward reward) {
        this.reward = reward;
    }

    public Long getCoinReward() {
        return coinReward;
    }

    public void setCoinReward(Long coinReward) {
        this.coinReward = coinReward;
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

    public String getQuestStatus() {
        return questStatus;
    }

    public void setQuestStatus(String questStatus) {
        this.questStatus = questStatus;
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
