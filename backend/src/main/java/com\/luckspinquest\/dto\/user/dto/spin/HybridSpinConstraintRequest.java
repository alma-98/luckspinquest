package com.luckspinquest.dto.user.dto.spin;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public record HybridSpinConstraintRequest(
        @NotNull
        Long userId,

        @NotNull
        Long wheelId,

        @NotNull
        List<Long> excludedSegmentIds
) {}
