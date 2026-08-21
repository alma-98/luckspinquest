package com.luckspinquest.repository;

import com.luckspinquest.entity.Quest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface QuestRepository
        extends JpaRepository<Quest, Long> {

    Optional<Quest> findByQuestCode(String questCode);

    List<Quest> findByQuestStatus(String questStatus);

    List<Quest> findByQuestStatusOrderByQuestNameAsc(
        String questStatus
    );

    List<Quest> findByQuestType(String questType);

    List<Quest> findByRewardRewardId(Long rewardId);

    boolean existsByQuestCode(String questCode);
}
