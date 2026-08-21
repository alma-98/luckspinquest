package com.luckspinquest.repository;

import com.luckspinquest.entity.SpinRuleVersion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpinRuleVersionRepository
        extends JpaRepository<SpinRuleVersion, Long> {

    List<SpinRuleVersion> findByWheelWheelIdOrderByVersionNumberDesc(
        Long wheelId
    );

    List<SpinRuleVersion> findByWheelWheelIdAndVersionStatus(
        Long wheelId,
        String versionStatus
    );

    List<SpinRuleVersion> findByCreatedByUserId(
        Long userId
    );
}
