package com.luckspinquest.dto.user.dto.spin;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record SpinRuleRequest(
        @NotNull
        Long segmentId,

        @NotNull
        @DecimalMin("0.0")
        @DecimalMax("100.0")
        BigDecimal probability
) {}
