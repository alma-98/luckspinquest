package com.luckspinquest.repository;

import com.luckspinquest.entity.EventParticipant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventParticipantRepository
        extends JpaRepository<EventParticipant, Long> {

    List<EventParticipant> findByEventEventIdOrderByRankPositionAsc(
        Long eventId
    );

    List<EventParticipant> findByUserUserIdOrderByJoinedAtDesc(
        Long userId
    );

    List<EventParticipant> findByEventEventIdAndUserUserId(
        Long eventId,
        Long userId
    );

    List<EventParticipant> findByEventEventIdAndParticipationStatusOrderByRankPositionAsc(
        Long eventId,
        String participationStatus
    );

    List<EventParticipant> findByUserUserIdAndParticipationStatusOrderByJoinedAtDesc(
        Long userId,
        String participationStatus
    );
}
