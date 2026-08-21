package com.luckspinquest.repository;

import com.luckspinquest.entity.AdminAction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface AdminActionRepository
        extends JpaRepository<AdminAction, Long> {

    List<AdminAction> findByAdminUserUserIdOrderByCreatedAtDesc(
        Long adminUserId
    );

    List<AdminAction> findByActionCodeOrderByCreatedAtDesc(
        String actionCode
    );

    List<AdminAction> findByActionTypeOrderByCreatedAtDesc(
        String actionType
    );

    List<AdminAction> findByActionCategoryOrderByCreatedAtDesc(
        String actionCategory
    );

    List<AdminAction> findByModuleOrderByCreatedAtDesc(
        String module
    );

    List<AdminAction> findByActionStatusOrderByCreatedAtDesc(
        String actionStatus
    );

    List<AdminAction> findByTargetTypeAndTargetIdOrderByCreatedAtDesc(
        String targetType,
        Long targetId
    );

    List<AdminAction> findByAdminUserUserIdAndModuleOrderByCreatedAtDesc(
        Long adminUserId,
        String module
    );

    List<AdminAction> findByCreatedAtBetweenOrderByCreatedAtDesc(
        LocalDateTime startAt,
        LocalDateTime endAt
    );
}
