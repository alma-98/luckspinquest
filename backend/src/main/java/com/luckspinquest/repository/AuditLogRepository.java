package com.luckspinquest.repository;

import com.luckspinquest.entity.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuditLogRepository
        extends JpaRepository<AuditLog, Long> {

    List<AuditLog> findByUserUserIdOrderByCreatedAtDesc(
        Long userId
    );

    List<AuditLog> findByActionOrderByCreatedAtDesc(
        String action
    );

    List<AuditLog> findByModuleOrderByCreatedAtDesc(
        String module
    );

    List<AuditLog> findByEntityTypeAndEntityIdOrderByCreatedAtDesc(
        String entityType,
        Long entityId
    );

    List<AuditLog> findByUserUserIdAndModuleOrderByCreatedAtDesc(
        Long userId,
        String module
    );
}
