package com.luckspinquest.repository;

import com.luckspinquest.entity.GameEconomyConfig;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GameEconomyConfigRepository
        extends JpaRepository<GameEconomyConfig, Long> {

    Optional<GameEconomyConfig> findByConfigKey(
        String configKey
    );

    List<GameEconomyConfig> findByConfigGroup(
        String configGroup
    );

    List<GameEconomyConfig> findByValueType(
        String valueType
    );

    List<GameEconomyConfig> findByConfigGroupAndIsEditableTrue(
        String configGroup
    );

    List<GameEconomyConfig> findByIsEditableTrue();

    List<GameEconomyConfig> findByUpdatedByUserId(
        Long userId
    );

    boolean existsByConfigKey(
        String configKey
    );
}
