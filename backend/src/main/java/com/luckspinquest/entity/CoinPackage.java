package com.luckspinquest.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(
    name = "coin_packages",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uq_coin_packages_code",
            columnNames = "package_code"
        )
    }
)
public class CoinPackage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "package_id")
    private Long packageId;

    @Column(
        name = "package_code",
        nullable = false,
        unique = true,
        length = 50
    )
    private String packageCode;

    @Column(name = "package_name", nullable = false, length = 100)
    private String packageName;

    @Column(name = "price_amount", nullable = false)
    private Long priceAmount;

    @Column(name = "coin_amount", nullable = false)
    private Long coinAmount;

    @Column(name = "bonus_coin", nullable = false)
    private Long bonusCoin = 0L;

    @Column(name = "total_coin", nullable = false)
    private Long totalCoin;

    @Column(name = "package_status", nullable = false, length = 20)
    private String packageStatus = "ACTIVE";

    @Column(name = "sort_order", nullable = false)
    private Integer sortOrder = 0;

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

    public Long getPackageId() {
        return packageId;
    }

    public void setPackageId(Long packageId) {
        this.packageId = packageId;
    }

    public String getPackageCode() {
        return packageCode;
    }

    public void setPackageCode(String packageCode) {
        this.packageCode = packageCode;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public Long getPriceAmount() {
        return priceAmount;
    }

    public void setPriceAmount(Long priceAmount) {
        this.priceAmount = priceAmount;
    }

    public Long getCoinAmount() {
        return coinAmount;
    }

    public void setCoinAmount(Long coinAmount) {
        this.coinAmount = coinAmount;
    }

    public Long getBonusCoin() {
        return bonusCoin;
    }

    public void setBonusCoin(Long bonusCoin) {
        this.bonusCoin = bonusCoin;
    }

    public Long getTotalCoin() {
        return totalCoin;
    }

    public void setTotalCoin(Long totalCoin) {
        this.totalCoin = totalCoin;
    }

    public String getPackageStatus() {
        return packageStatus;
    }

    public void setPackageStatus(String packageStatus) {
        this.packageStatus = packageStatus;
    }

    public Integer getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
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
