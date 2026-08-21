package com.luckspinquest.dto.quest;

import java.time.LocalDateTime;

public record QuestResponse(
        Long questId,
        String questCode,
        String questName,
        String questDescription,
        String questType,
        Long targetValue,
        Long rewardAmount,
        String rewardType,
        String questStatus,
        LocalDateTime startAt,
        LocalDateTime endAt
) {
}
