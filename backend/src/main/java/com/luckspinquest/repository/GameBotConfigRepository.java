package com.luckspinquest.repository;

import com.luckspinquest.entity.GameBotConfig;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GameBotConfigRepository
        extends JpaRepository<GameBotConfig, Long> {

    List<GameBotConfig> findByBotBotId(Long botId);

    List<GameBotConfig> findByBotBotIdAndActiveTrue(
        Long botId
    );

    List<GameBotConfig> findByBotBotIdAndConfigKey(
        Long botId,
        String configKey
    );

    List<GameBotConfig> findByBotBotIdAndValueType(
        Long botId,
        String valueType
    );
}
