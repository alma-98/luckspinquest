package com.luckspinquest.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(
    name = "withdrawal_transactions",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uq_withdrawal_transactions_reference",
            columnNames = "withdrawal_reference"
        )
    }
)
public class WithdrawalTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "withdrawal_id")
    private Long withdrawalId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
        name = "user_id",
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_withdrawal_transactions_user")
    )
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
        name = "wallet_id",
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_withdrawal_transactions_wallet")
    )
    private Wallet wallet;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "payment_account_id",
        foreignKey = @ForeignKey(name = "fk_withdrawal_transactions_payment_account")
    )
    private PaymentAccount paymentAccount;

    @Column(name = "payment_type", nullable = false, length = 20)
    private String paymentType;

    @Column(name = "account_name", nullable = false, length = 100)
    private String accountName;

    @Column(name = "account_number", nullable = false, length = 100)
    private String accountNumber;

    @Column(name = "bank_code", length = 20)
    private String bankCode;

    @Column(name = "withdrawal_coin_amount", nullable = false)
    private Long withdrawalCoinAmount;

    @Column(
        name = "withdrawal_rate",
        nullable = false,
        precision = 20,
        scale = 6
    )
    private BigDecimal withdrawalRate;

    @Column(name = "withdrawal_gross_amount", nullable = false)
    private Long withdrawalGrossAmount;

    @Column(name = "withdrawal_fee", nullable = false)
    private Long withdrawalFee = 0L;

    @Column(name = "withdrawal_net_amount", nullable = false)
    private Long withdrawalNetAmount;

    @Column(name = "withdrawal_status", nullable = false, length = 20)
    private String withdrawalStatus = "PENDING";

    @Column(name = "withdrawal_reference", length = 100, unique = true)
    private String withdrawalReference;

    @Column(name = "admin_note", columnDefinition = "TEXT")
    private String adminNote;

    @Column(name = "requested_at", nullable = false)
    private LocalDateTime requestedAt;

    @Column(name = "processed_at")
    private LocalDateTime processedAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();

        if (requestedAt == null) {
            requestedAt = now;
        }

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

    public Long getWithdrawalId() {
        return withdrawalId;
    }

    public void setWithdrawalId(Long withdrawalId) {
        this.withdrawalId = withdrawalId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Wallet getWallet() {
        return wallet;
    }

    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
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

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public Long getWithdrawalCoinAmount() {
        return withdrawalCoinAmount;
    }

    public void setWithdrawalCoinAmount(Long withdrawalCoinAmount) {
        this.withdrawalCoinAmount = withdrawalCoinAmount;
    }

    public BigDecimal getWithdrawalRate() {
        return withdrawalRate;
    }

    public void setWithdrawalRate(BigDecimal withdrawalRate) {
        this.withdrawalRate = withdrawalRate;
    }

    public Long getWithdrawalGrossAmount() {
        return withdrawalGrossAmount;
    }

    public void setWithdrawalGrossAmount(Long withdrawalGrossAmount) {
        this.withdrawalGrossAmount = withdrawalGrossAmount;
    }

    public Long getWithdrawalFee() {
        return withdrawalFee;
    }

    public void setWithdrawalFee(Long withdrawalFee) {
        this.withdrawalFee = withdrawalFee;
    }

    public Long getWithdrawalNetAmount() {
        return withdrawalNetAmount;
    }

    public void setWithdrawalNetAmount(Long withdrawalNetAmount) {
        this.withdrawalNetAmount = withdrawalNetAmount;
    }

    public String getWithdrawalStatus() {
        return withdrawalStatus;
    }

    public void setWithdrawalStatus(String withdrawalStatus) {
        this.withdrawalStatus = withdrawalStatus;
    }

    public String getWithdrawalReference() {
        return withdrawalReference;
    }

    public void setWithdrawalReference(String withdrawalReference) {
        this.withdrawalReference = withdrawalReference;
    }

    public String getAdminNote() {
        return adminNote;
    }

    public void setAdminNote(String adminNote) {
        this.adminNote = adminNote;
    }

    public LocalDateTime getRequestedAt() {
        return requestedAt;
    }

    public void setRequestedAt(LocalDateTime requestedAt) {
        this.requestedAt = requestedAt;
    }

    public LocalDateTime getProcessedAt() {
        return processedAt;
    }

    public void setProcessedAt(LocalDateTime processedAt) {
        this.processedAt = processedAt;
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

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
