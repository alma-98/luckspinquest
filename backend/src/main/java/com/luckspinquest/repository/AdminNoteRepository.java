package com.luckspinquest.repository;

import com.luckspinquest.entity.AdminNote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdminNoteRepository
        extends JpaRepository<AdminNote, Long> {

    List<AdminNote> findByAdminUserUserIdOrderByCreatedAtDesc(
        Long adminUserId
    );

    List<AdminNote> findByTargetTypeAndTargetIdOrderByCreatedAtDesc(
        String targetType,
        Long targetId
    );

    List<AdminNote> findByNoteStatusOrderByCreatedAtDesc(
        String noteStatus
    );

    List<AdminNote> findByAdminUserUserIdAndNoteStatusOrderByCreatedAtDesc(
        Long adminUserId,
        String noteStatus
    );

    List<AdminNote> findByTargetTypeAndNoteStatusOrderByCreatedAtDesc(
        String targetType,
        String noteStatus
    );
}
