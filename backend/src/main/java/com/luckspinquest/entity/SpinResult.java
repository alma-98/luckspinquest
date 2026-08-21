package com.luckspinquest.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(
    name = "spin_results",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uq_spin_results_reference",
            columnNames = "result_reference"
        )
    }
)
public class SpinResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "spin_result_id")
    private Long spinResultId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
        name = "spin_session_id",
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_spin_results_spin_session")
    )
    private SpinSession spinSession;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
        name = "user_id",
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_spin_results_user")
    )
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
        name = "wheel_id",
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_spin_results_wheel")
    )
    private SpinWheel wheel;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
        name = "segment_id",
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_spin_results_segment")
    )
    private SpinSegment segment;

    @Column(name = "result_type", nullable = false, length = 30)
    private String resultType;

    @Column(name = "coin_cost", nullable = false)
    private Long coinCost;

    @Column(name = "coin_reward", nullable = false)
    private Long coinReward = 0L;

    @Column(name = "random_value", precision = 20, scale = 10)
    private BigDecimal randomValue;

    @Column(
        name = "result_reference",
        nullable = false,
        unique = true,
        length = 100
    )
    private String resultReference;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }

    public Long getSpinResultId() {
        return spinResultId;
    }

    public void setSpinResultId(Long spinResultId) {
        this.spinResultId = spinResultId;
    }

    public SpinSession getSpinSession() {
        return spinSession;
    }

    public void setSpinSession(SpinSession spinSession) {
        this.spinSession = spinSession;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public SpinWheel getWheel() {
        return wheel;
    }

    public void setWheel(SpinWheel wheel) {
        this.wheel = wheel;
    }

    public SpinSegment getSegment() {
        return segment;
    }

    public void setSegment(SpinSegment segment) {
        this.segment = segment;
    }

    public String getResultType() {
        return resultType;
    }

    public void setResultType(String resultType) {
        this.resultType = resultType;
    }

    public Long getCoinCost() {
        return coinCost;
    }

    public void setCoinCost(Long coinCost) {
        this.coinCost = coinCost;
    }

    public Long getCoinReward() {
        return coinReward;
    }

    public void setCoinReward(Long coinReward) {
        this.coinReward = coinReward;
    }

    public BigDecimal getRandomValue() {
        return randomValue;
    }

    public void setRandomValue(BigDecimal randomValue) {
        this.randomValue = randomValue;
    }

    public String getResultReference() {
        return resultReference;
    }

    public void setResultReference(String resultReference) {
        this.resultReference = resultReference;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
