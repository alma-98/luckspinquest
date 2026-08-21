package com.luckspinquest.repository;

import com.luckspinquest.entity.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PasswordResetTokenRepository
        extends JpaRepository<PasswordResetToken, Long> {

    Optional<PasswordResetToken> findByTokenHash(
        String tokenHash
    );

    List<PasswordResetToken> findByUserUserIdOrderByCreatedAtDesc(
        Long userId
    );

    List<PasswordResetToken> findByUserUserIdAndUsedAtIsNullOrderByCreatedAtDesc(
        Long userId
    );

    List<PasswordResetToken> findByUserUserIdAndExpiresAtAfterAndUsedAtIsNull(
        Long userId,
        LocalDateTime now
    );

    List<PasswordResetToken> findByExpiresAtBefore(
        LocalDateTime now
    );

    List<PasswordResetToken> findByUsedAtIsNotNull();

    boolean existsByTokenHash(
        String tokenHash
    );
}
