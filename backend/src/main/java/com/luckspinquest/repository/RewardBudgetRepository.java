package com.luckspinquest.repository;

import com.luckspinquest.entity.RewardBudget;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface RewardBudgetRepository
        extends JpaRepository<RewardBudget, Long> {

    List<RewardBudget> findByBudgetType(
        String budgetType
    );

    List<RewardBudget> findByBudgetStatus(
        String budgetStatus
    );

    List<RewardBudget> findByBudgetStatusOrderByStartAtDesc(
        String budgetStatus
    );

    List<RewardBudget> findByBudgetTypeAndBudgetStatus(
        String budgetType,
        String budgetStatus
    );

    List<RewardBudget> findByStartAtBetween(
        LocalDateTime startAt,
        LocalDateTime endAt
    );

    List<RewardBudget> findByEndAtBetween(
        LocalDateTime startAt,
        LocalDateTime endAt
    );
}
