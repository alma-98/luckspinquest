package com.luckspinquest.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "topup_transactions")
public class TopupTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "topup_id")
    private Long topupId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
        name = "user_id",
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_topup_transactions_user")
    )
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "payment_account_id",
        foreignKey = @ForeignKey(name = "fk_topup_transactions_payment_account")
    )
    private PaymentAccount paymentAccount;

    @Column(name = "payment_type", nullable = false, length = 20)
    private String paymentType;

    @Column(name = "topup_amount", nullable = false)
    private Long topupAmount;

    @Column(name = "coin_amount", nullable = false)
    private Long coinAmount = 0L;

    @Column(name = "bonus_coin", nullable = false)
    private Long bonusCoin = 0L;

    @Column(name = "total_coin", nullable = false)
    private Long totalCoin = 0L;

    @Column(name = "payment_reference", length = 100)
    private String paymentReference;

    @Column(name = "topup_status", nullable = false, length = 20)
    private String topupStatus = "PENDING";

    @Column(name = "expired_at")
    private LocalDateTime expiredAt;

    @Column(name = "paid_at")
    private LocalDateTime paidAt;

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

    public Long getTopupId() {
        return topupId;
    }

    public void setTopupId(Long topupId) {
        this.topupId = topupId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public PaymentAccount getPaymentAccount() {
        return paymentAccount;
    }

    public void setPaymentAccount(PaymentAccount paymentAccount) {
        this.paymentAccount = paymentAccount;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public Long getTopupAmount() {
        return topupAmount;
    }

    public void setTopupAmount(Long topupAmount) {
        this.topupAmount = topupAmount;
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

    public String getPaymentReference() {
        return paymentReference;
    }

    public void setPaymentReference(String paymentReference) {
        this.paymentReference = paymentReference;
    }

    public String getTopupStatus() {
        return topupStatus;
    }

    public void setTopupStatus(String topupStatus) {
        this.topupStatus = topupStatus;
    }

    public LocalDateTime getExpiredAt() {
        return expiredAt;
    }

    public void setExpiredAt(LocalDateTime expiredAt) {
        this.expiredAt = expiredAt;
    }

    public LocalDateTime getPaidAt() {
        return paidAt;
    }

    public void setPaidAt(LocalDateTime paidAt) {
        this.paidAt = paidAt;
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
