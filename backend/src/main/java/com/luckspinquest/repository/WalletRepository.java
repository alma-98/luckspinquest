package com.luckspinquest.repository;

import com.luckspinquest.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WalletRepository extends JpaRepository<Wallet, Long> {

    Optional<Wallet> findByUserUserId(Long userId);

    boolean existsByUserUserId(Long userId);
}
