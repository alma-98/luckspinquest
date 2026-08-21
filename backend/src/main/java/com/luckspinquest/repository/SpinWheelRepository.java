package com.luckspinquest.repository;

import com.luckspinquest.entity.SpinWheel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SpinWheelRepository
        extends JpaRepository<SpinWheel, Long> {

    Optional<SpinWheel> findByWheelCode(String wheelCode);

    List<SpinWheel> findByWheelStatus(String wheelStatus);

    List<SpinWheel> findByWheelStatusOrderByWheelNameAsc(
        String wheelStatus
    );

    boolean existsByWheelCode(String wheelCode);
}
