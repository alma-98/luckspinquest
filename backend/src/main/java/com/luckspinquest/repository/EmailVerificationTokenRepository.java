package com.luckspinquest.repository;

import com.luckspinquest.entity.EmailVerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EmailVerificationTokenRepository
        extends JpaRepository<EmailVerificationToken, Long> {

    Optional<EmailVerificationToken> findByTokenHash(
        String tokenHash
    );

    List<EmailVerificationToken> findByUserUserIdOrderByCreatedAtDesc(
        Long userId
    );

    List<EmailVerificationToken> findByUserUserIdAndVerifiedAtIsNullOrderByCreatedAtDesc(
        Long userId
    );

    List<EmailVerificationToken> findByUserUserIdAndExpiresAtAfterAndVerifiedAtIsNull(
        Long userId,
        LocalDateTime now
    );

    List<EmailVerificationToken> findByExpiresAtBefore(
        LocalDateTime now
    );

    List<EmailVerificationToken> findByVerifiedAtIsNotNull();

    boolean existsByTokenHash(
        String tokenHash
    );
}
