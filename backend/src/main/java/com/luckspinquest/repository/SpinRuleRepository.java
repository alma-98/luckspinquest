package com.luckspinquest.repository;

import com.luckspinquest.entity.SpinRule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpinRuleRepository
        extends JpaRepository<SpinRule, Long> {

    List<SpinRule> findByWheelWheelId(Long wheelId);

    List<SpinRule> findByWheelWheelIdAndRuleStatus(
        Long wheelId,
        String ruleStatus
    );

    List<SpinRule> findByWheelWheelIdAndRuleType(
        Long wheelId,
        String ruleType
    );

    List<SpinRule> findByRuleStatus(String ruleStatus);
}
