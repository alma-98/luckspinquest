package com.luckspinquest.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(
    name = "referrals",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "referrals_referred_user_id_key",
            columnNames = "referred_user_id"
        )
    }
)
public class Referral {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "referral_id")
    private Long referralId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
        name = "referral_code_id",
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_referrals_referral_code")
    )
    private ReferralCode referralCode;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
        name = "referrer_user_id",
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_referrals_referrer_user")
    )
    private User referrerUser;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
        name = "referred_user_id",
        nullable = false,
        unique = true,
        foreignKey = @ForeignKey(name = "fk_referrals_referred")
    )
    private User referredUser;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "reward_id",
        foreignKey = @ForeignKey(name = "fk_referrals_reward")
    )
    private Reward reward;

    @Column(name = "coin_reward", nullable = false)
    private Long coinReward = 0L;

    @Column(name = "referral_status", nullable = false, length = 20)
    private String referralStatus = "PENDING";

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }

    public Long getReferralId() {
        return referralId;
    }

    public void setReferralId(Long referralId) {
        this.referralId = referralId;
    }

    public ReferralCode getReferralCode() {
        return referralCode;
    }

    public void setReferralCode(ReferralCode referralCode) {
        this.referralCode = referralCode;
    }

    public User getReferrerUser() {
        return referrerUser;
    }

    public void setReferrerUser(User referrerUser) {
        this.referrerUser = referrerUser;
    }

    public User getReferredUser() {
        return referredUser;
    }

    public void setReferredUser(User referredUser) {
        this.referredUser = referredUser;
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

    public String getReferralStatus() {
        return referralStatus;
    }

    public void setReferralStatus(String referralStatus) {
        this.referralStatus = referralStatus;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
