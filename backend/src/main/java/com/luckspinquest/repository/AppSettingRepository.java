package com.luckspinquest.repository;

import com.luckspinquest.entity.AppSetting;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AppSettingRepository
        extends JpaRepository<AppSetting, Long> {

    Optional<AppSetting> findBySettingKey(
        String settingKey
    );

    List<AppSetting> findBySettingGroup(
        String settingGroup
    );

    List<AppSetting> findByValueType(
        String valueType
    );

    List<AppSetting> findByIsEditableTrue();

    List<AppSetting> findBySettingGroupAndIsEditableTrue(
        String settingGroup
    );

    List<AppSetting> findByUpdatedByUserId(
        Long userId
    );

    boolean existsBySettingKey(
        String settingKey
    );
}
