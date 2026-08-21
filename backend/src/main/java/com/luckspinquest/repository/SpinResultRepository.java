package com.luckspinquest.repository;

import com.luckspinquest.entity.SpinResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SpinResultRepository
        extends JpaRepository<SpinResult, Long> {

    Optional<SpinResult> findByResultReference(
        String resultReference
    );

    List<SpinResult> findBySpinSessionSpinSessionIdOrderByCreatedAtDesc(
        Long spinSessionId
    );

    List<SpinResult> findByUserUserIdOrderByCreatedAtDesc(
        Long userId
    );

    List<SpinResult> findByWheelWheelIdOrderByCreatedAtDesc(
        Long wheelId
    );

    List<SpinResult> findBySegmentSegmentId(
        Long segmentId
    );

    List<SpinResult> findByUserUserIdAndResultTypeOrderByCreatedAtDesc(
        Long userId,
        String resultType
    );

    boolean existsByResultReference(
        String resultReference
    );
}
