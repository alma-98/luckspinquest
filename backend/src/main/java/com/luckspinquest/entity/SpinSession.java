package com.luckspinquest.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(
    name = "spin_sessions",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uq_spin_sessions_reference",
            columnNames = "session_reference"
        )
    }
)
public class SpinSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "spin_session_id")
    private Long spinSessionId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
        name = "user_id",
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_spin_sessions_user")
    )
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
        name = "wheel_id",
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_spin_sessions_wheel")
    )
    private SpinWheel wheel;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
        name = "rule_version_id",
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_spin_sessions_rule_version")
    )
    private SpinRuleVersion ruleVersion;

    @Column(
        name = "session_reference",
        nullable = false,
        unique = true,
        length = 100
    )
    private String sessionReference;

    @Column(name = "spin_count", nullable = false)
    private Integer spinCount = 0;

    @Column(name = "started_at", nullable = false)
    private LocalDateTime startedAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Column(name = "session_status", nullable = false, length = 20)
    private String sessionStatus;

    @PrePersist
    protected void onCreate() {
        if (startedAt == null) {
            startedAt = LocalDateTime.now();
        }
    }

    public Long getSpinSessionId() {
        return spinSessionId;
    }

    public void setSpinSessionId(Long spinSessionId) {
        this.spinSessionId = spinSessionId;
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

    public SpinRuleVersion getRuleVersion() {
        return ruleVersion;
    }

    public void setRuleVersion(SpinRuleVersion ruleVersion) {
        this.ruleVersion = ruleVersion;
    }

    public String getSessionReference() {
        return sessionReference;
    }

    public void setSessionReference(String sessionReference) {
        this.sessionReference = sessionReference;
    }

    public Integer getSpinCount() {
        return spinCount;
    }

    public void setSpinCount(Integer spinCount) {
        this.spinCount = spinCount;
    }

    public LocalDateTime getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(LocalDateTime startedAt) {
        this.startedAt = startedAt;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }

    public String getSessionStatus() {
        return sessionStatus;
    }

    public void setSessionStatus(String sessionStatus) {
        this.sessionStatus = sessionStatus;
    }
}
