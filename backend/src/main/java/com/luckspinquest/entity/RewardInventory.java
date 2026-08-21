package com.luckspinquest.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reward_inventory")
public class RewardInventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "inventory_id")
    private Long inventoryId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
        name = "reward_id",
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_reward_inventory_reward")
    )
    private Reward reward;

    @Column(name = "quantity_total", nullable = false)
    private Long quantityTotal = 0L;

    @Column(name = "quantity_available", nullable = false)
    private Long quantityAvailable = 0L;

    @Column(name = "quantity_reserved", nullable = false)
    private Long quantityReserved = 0L;

    @Column(name = "quantity_redeemed", nullable = false)
    private Long quantityRedeemed = 0L;

    @Column(name = "inventory_status", nullable = false, length = 20)
    private String inventoryStatus = "ACTIVE";

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        if (updatedAt == null) {
            updatedAt = LocalDateTime.now();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public Long getInventoryId() {
        return inventoryId;
    }

    public void setInventoryId(Long inventoryId) {
        this.inventoryId = inventoryId;
    }

    public Reward getReward() {
        return reward;
    }

    public void setReward(Reward reward) {
        this.reward = reward;
    }

    public Long getQuantityTotal() {
        return quantityTotal;
    }

    public void setQuantityTotal(Long quantityTotal) {
        this.quantityTotal = quantityTotal;
    }

    public Long getQuantityAvailable() {
        return quantityAvailable;
    }

    public void setQuantityAvailable(Long quantityAvailable) {
        this.quantityAvailable = quantityAvailable;
    }

    public Long getQuantityReserved() {
        return quantityReserved;
    }

    public void setQuantityReserved(Long quantityReserved) {
        this.quantityReserved = quantityReserved;
    }

    public Long getQuantityRedeemed() {
        return quantityRedeemed;
    }

    public void setQuantityRedeemed(Long quantityRedeemed) {
        this.quantityRedeemed = quantityRedeemed;
    }

    public String getInventoryStatus() {
        return inventoryStatus;
    }

    public void setInventoryStatus(String inventoryStatus) {
        this.inventoryStatus = inventoryStatus;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
