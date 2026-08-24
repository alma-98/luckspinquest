package com.luckspinquest.service;

import com.luckspinquest.dto.spin.PlaySpinRequest;
import com.luckspinquest.dto.spin.SpinResponse;
import org.springframework.stereotype.Service;

@Service
public class SpinService {

    public SpinResponse play(Long userId, PlaySpinRequest request) {

        if (request == null || request.getSpinId() == null
                || request.getSpinId().isBlank()) {
            throw new IllegalArgumentException("spinId is required");
        }

        /*
         * Spin Engine will be implemented here.
         *
         * Current responsibilities:
         * - validate spin configuration
         * - load active rule version
         * - generate secure random value
         * - select segment based on probability
         * - create SpinSession
         * - create SpinResult
         * - settle reward / wallet transaction
         */

        throw new UnsupportedOperationException(
                "Spin Engine is not implemented yet"
        );
    }
}
