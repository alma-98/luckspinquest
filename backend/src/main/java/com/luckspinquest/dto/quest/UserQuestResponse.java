package com.luckspinquest.dto.quest;

import java.time.LocalDateTime;

public record UserQuestResponse(
        Long userQuestId,
        Long userId,
        Long questId,
        Long progressValue,
        Long targetValue,
        String questStatus,
        LocalDateTime startedAt,
        LocalDateTime completedAt,
        LocalDateTime claimedAt
) {
}
