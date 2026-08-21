package com.luckspinquest.repository;

import com.luckspinquest.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface RefreshTokenRepository
        extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByTokenHash(
        String tokenHash
    );

    List<RefreshToken> findByUserUserIdOrderByCreatedAtDesc(
        Long userId
    );

    List<RefreshToken> findByUserUserIdAndRevokedAtIsNullOrderByCreatedAtDesc(
        Long userId
    );

    List<RefreshToken> findByUserUserIdAndExpiresAtAfterAndRevokedAtIsNull(
        Long userId,
        LocalDateTime now
    );

    List<RefreshToken> findByExpiresAtBefore(
        LocalDateTime now
    );

    List<RefreshToken> findByRevokedAtIsNotNull();

    boolean existsByTokenHash(
        String tokenHash
    );
}
