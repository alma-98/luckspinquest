package com.luckspinquest.repository;

import com.luckspinquest.entity.PlayerGameConfig;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlayerGameConfigRepository
        extends JpaRepository<PlayerGameConfig, Long> {

    List<PlayerGameConfig> findByUserUserId(Long userId);

    List<PlayerGameConfig> findByUserUserIdAndConfigKey(
        Long userId,
        String configKey
    );

    List<PlayerGameConfig> findByUserUserIdAndValueType(
        Long userId,
        String valueType
    );
}
