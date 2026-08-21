package com.luckspinquest.repository;

import com.luckspinquest.entity.SpinSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SpinSessionRepository
        extends JpaRepository<SpinSession, Long> {

    long countByUserUserId(Long userId);

    Optional<SpinSession> findBySessionReference(
        String sessionReference
    );

    List<SpinSession> findByUserUserIdOrderByStartedAtDesc(
        Long userId
    );

    List<SpinSession> findByWheelWheelIdOrderByStartedAtDesc(
        Long wheelId
    );

    List<SpinSession> findByUserUserIdAndSessionStatusOrderByStartedAtDesc(
        Long userId,
        String sessionStatus
    );

    List<SpinSession> findByRuleVersionVersionId(
        Long versionId
    );

    boolean existsBySessionReference(
        String sessionReference
    );
}
