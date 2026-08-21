package com.luckspinquest.repository;

import com.luckspinquest.entity.SpinSegment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SpinSegmentRepository
        extends JpaRepository<SpinSegment, Long> {

    List<SpinSegment> findByWheelWheelIdOrderByDisplayOrderAsc(
        Long wheelId
    );

    List<SpinSegment> findByWheelWheelIdAndActiveTrueOrderByDisplayOrderAsc(
        Long wheelId
    );

    List<SpinSegment> findByWheelWheelIdAndSegmentType(
        Long wheelId,
        String segmentType
    );

    List<SpinSegment> findByRewardRewardId(
        Long rewardId
    );
}
