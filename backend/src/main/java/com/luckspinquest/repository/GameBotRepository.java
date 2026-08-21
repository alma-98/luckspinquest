package com.luckspinquest.repository;

import com.luckspinquest.entity.GameBot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GameBotRepository
        extends JpaRepository<GameBot, Long> {

    Optional<GameBot> findByBotCode(String botCode);

    List<GameBot> findByBotStatus(String botStatus);

    List<GameBot> findByBotStatusOrderByBotNameAsc(
        String botStatus
    );

    List<GameBot> findByBotType(String botType);

    boolean existsByBotCode(String botCode);
}
