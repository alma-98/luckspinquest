package com.luckspinquest.repository;

import com.luckspinquest.entity.AccountRestriction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface AccountRestrictionRepository
        extends JpaRepository<AccountRestriction, Long> {

    List<AccountRestriction> findByUserUserIdOrderByStartAtDesc(
        Long userId
    );

    List<AccountRestriction> findByUserUserIdAndRestrictionStatusOrderByStartAtDesc(
        Long userId,
        String restrictionStatus
    );

    List<AccountRestriction> findByRestrictionType(
        String restrictionType
    );

    List<AccountRestriction> findByRestrictionStatus(
        String restrictionStatus
    );

    List<AccountRestriction> findByCreatedByUserId(
        Long userId
    );

    List<AccountRestriction> findByStartAtBetween(
        LocalDateTime startAt,
        LocalDateTime endAt
    );
}
