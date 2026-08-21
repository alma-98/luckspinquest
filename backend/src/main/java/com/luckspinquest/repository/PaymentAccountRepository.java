package com.luckspinquest.repository;

import com.luckspinquest.entity.PaymentAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PaymentAccountRepository
        extends JpaRepository<PaymentAccount, Long> {

    List<PaymentAccount> findByUserUserId(Long userId);

    List<PaymentAccount> findByUserUserIdAndStatus(
        Long userId,
        String status
    );

    Optional<PaymentAccount> findByUserUserIdAndPrimaryTrue(
        Long userId
    );

    List<PaymentAccount> findByUserUserIdAndPaymentType(
        Long userId,
        String paymentType
    );
}
