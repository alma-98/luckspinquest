package com.luckspinquest.dto.wallet;

import java.time.LocalDateTime;

public record WalletResponse(
        Long walletId,
        Long userId,
        Long balance,
        Long lockedBalance,
        Long availableBalance,
        String currency,
        String status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
