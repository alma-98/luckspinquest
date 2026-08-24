package com.luckspinquest.dto.user.service.spin;

import com.luckspinquest.dto.user.entity.spin.SpinMode;

public interface SpinModeService {

    void validateMode(SpinMode mode);

    SpinMode normalize(SpinMode mode);
}
