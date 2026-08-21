package com.luckspinquest.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(
    name = "spin_wheels",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uq_spin_wheels_code",
            columnNames = "wheel_code"
        )
    }
)
public class SpinWheel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wheel_id")
    private Long wheelId;

    @Column(
        name = "wheel_code",
        nullable = false,
        unique = true,
        length = 50
    )
    private String wheelCode;

    @Column(name = "wheel_name", nullable = false, length = 100)
    private String wheelName;

    @Column(name = "wheel_description", columnDefinition = "TEXT")
    private String wheelDescription;

    @Column(name = "spin_cost", nullable = false)
    private Long spinCost = 0L;

    @Column(name = "daily_spin_limit")
    private Integer dailySpinLimit;

    @Column(name = "wheel_status", nullable = false, length = 20)
    private String wheelStatus = "ACTIVE";

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

    public Long getWheelId() {
        return wheelId;
    }

    public void setWheelId(Long wheelId) {
        this.wheelId = wheelId;
    }

    public String getWheelCode() {
        return wheelCode;
    }

    public void setWheelCode(String wheelCode) {
        this.wheelCode = wheelCode;
    }

    public String getWheelName() {
        return wheelName;
    }

    public void setWheelName(String wheelName) {
        this.wheelName = wheelName;
    }

    public String getWheelDescription() {
        return wheelDescription;
    }

    public void setWheelDescription(String wheelDescription) {
        this.wheelDescription = wheelDescription;
    }

    public Long getSpinCost() {
        return spinCost;
    }

    public void setSpinCost(Long spinCost) {
        this.spinCost = spinCost;
    }

    public Integer getDailySpinLimit() {
        return dailySpinLimit;
    }

    public void setDailySpinLimit(Integer dailySpinLimit) {
        this.dailySpinLimit = dailySpinLimit;
    }

    public String getWheelStatus() {
        return wheelStatus;
    }

    public void setWheelStatus(String wheelStatus) {
        this.wheelStatus = wheelStatus;
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
