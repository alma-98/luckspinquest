package com.luckspinquest.repository;

import com.luckspinquest.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository
        extends JpaRepository<Notification, Long> {

    List<Notification> findByUserUserIdOrderByCreatedAtDesc(
        Long userId
    );

    List<Notification> findByUserUserIdAndIsReadFalseOrderByCreatedAtDesc(
        Long userId
    );

    List<Notification> findByUserUserIdAndIsReadTrueOrderByCreatedAtDesc(
        Long userId
    );

    List<Notification> findByUserUserIdAndNotificationTypeOrderByCreatedAtDesc(
        Long userId,
        String notificationType
    );

    List<Notification> findByReferenceTypeAndReferenceId(
        String referenceType,
        Long referenceId
    );

    long countByUserUserIdAndIsReadFalse(
        Long userId
    );
}
