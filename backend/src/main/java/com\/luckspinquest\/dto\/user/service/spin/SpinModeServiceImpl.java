package com.luckspinquest.dto.user.service.spin;

import com.luckspinquest.dto.user.entity.spin.SpinMode;
import org.springframework.stereotype.Service;

@Service
public class SpinModeServiceImpl implements SpinModeService {

    @Override
    public void validateMode(SpinMode mode) {
        if (mode == null) {
            throw new IllegalArgumentException("spinMode is required");
        }
    }

    @Override
    public SpinMode normalize(SpinMode mode) {
        validateMode(mode);
        return mode;
    }
}
