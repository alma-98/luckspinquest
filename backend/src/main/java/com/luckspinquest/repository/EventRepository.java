package com.luckspinquest.repository;

import com.luckspinquest.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EventRepository
        extends JpaRepository<Event, Long> {

    Optional<Event> findByEventCode(String eventCode);

    List<Event> findByEventStatus(String eventStatus);

    List<Event> findByEventStatusOrderByStartAtAsc(
        String eventStatus
    );

    List<Event> findByEventType(String eventType);

    List<Event> findByStartAtBetween(
        LocalDateTime startAt,
        LocalDateTime endAt
    );

    boolean existsByEventCode(String eventCode);
}
