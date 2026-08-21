package com.luckspinquest.repository;

import com.luckspinquest.entity.RiskFlag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RiskFlagRepository
        extends JpaRepository<RiskFlag, Long> {

    List<RiskFlag> findByUserUserIdOrderByCreatedAtDesc(
        Long userId
    );

    List<RiskFlag> findByUserUserIdAndRiskStatusOrderByCreatedAtDesc(
        Long userId,
        String riskStatus
    );

    List<RiskFlag> findByRiskType(
        String riskType
    );

    List<RiskFlag> findByRiskLevel(
        String riskLevel
    );

    List<RiskFlag> findByRiskStatus(
        String riskStatus
    );

    List<RiskFlag> findByReviewedByUserId(
        Long userId
    );
}
