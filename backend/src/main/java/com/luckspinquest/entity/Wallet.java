package com.luckspinquest.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(
    name = "wallets",
    uniqueConstraints = {
        @UniqueConstraint(name = "uq_wallets_user_id", columnNames = "user_id")
    }
)
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wallet_id")
    private Long walletId;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
        name = "user_id",
        nullable = false,
        unique = true,
        foreignKey = @ForeignKey(name = "fk_wallets_user")
    )
    private User user;

    @Column(name = "wallet_balance", nullable = false)
    private Long walletBalance = 0L;

    @Column(name = "wallet_locked_balance", nullable = false)
    private Long walletLockedBalance = 0L;

    @Column(name = "wallet_available_balance", nullable = false)
    private Long walletAvailableBalance = 0L;

    @Column(name = "wallet_currency", nullable = false, length = 20)
    private String walletCurrency = "COIN";

    @Column(name = "wallet_status", nullable = false, length = 20)
    private String walletStatus = "ACTIVE";

    @Column(name = "created_at", nullable = false, updatable = false)
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

    public Long getWalletId() {
        return walletId;
    }

    public void setWalletId(Long walletId) {
        this.walletId = walletId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getWalletBalance() {
        return walletBalance;
    }

    public void setWalletBalance(Long walletBalance) {
        this.walletBalance = walletBalance;
    }

    public Long getWalletLockedBalance() {
        return walletLockedBalance;
    }

    public void setWalletLockedBalance(Long walletLockedBalance) {
        this.walletLockedBalance = walletLockedBalance;
    }

    public Long getWalletAvailableBalance() {
        return walletAvailableBalance;
    }

    public void setWalletAvailableBalance(Long walletAvailableBalance) {
        this.walletAvailableBalance = walletAvailableBalance;
    }

    public String getWalletCurrency() {
        return walletCurrency;
    }

    public void setWalletCurrency(String walletCurrency) {
        this.walletCurrency = walletCurrency;
    }

    public String getWalletStatus() {
        return walletStatus;
    }

    public void setWalletStatus(String walletStatus) {
        this.walletStatus = walletStatus;
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
