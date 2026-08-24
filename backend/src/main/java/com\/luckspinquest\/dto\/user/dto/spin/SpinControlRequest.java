package com.luckspinquest.dto.user.dto.spin;

import com.luckspinquest.dto.user.entity.spin.SpinControlType;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;

public record SpinControlRequest(
        @NotNull
        Long userId,

        @NotNull
        Long wheelId,

        Long segmentId,

        @NotNull
        SpinControlType controlType,

        Integer targetSpinNumber,

        Instant effectiveFrom,

        Instant expiresAt
) {}
