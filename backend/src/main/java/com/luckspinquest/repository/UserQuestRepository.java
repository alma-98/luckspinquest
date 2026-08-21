package com.luckspinquest.repository;

import com.luckspinquest.entity.UserQuest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserQuestRepository
        extends JpaRepository<UserQuest, Long> {

    List<UserQuest> findByUserUserIdOrderByStartedAtDesc(
        Long userId
    );

    List<UserQuest> findByQuestQuestIdOrderByStartedAtDesc(
        Long questId
    );

    List<UserQuest> findByUserUserIdAndQuestQuestId(
        Long userId,
        Long questId
    );

    List<UserQuest> findByUserUserIdAndQuestStatusOrderByStartedAtDesc(
        Long userId,
        String questStatus
    );

    List<UserQuest> findByQuestStatusOrderByStartedAtDesc(
        String questStatus
    );
}
