package com.luckspinquest.service;

import com.luckspinquest.dto.quest.QuestResponse;
import com.luckspinquest.dto.quest.UserQuestResponse;
import com.luckspinquest.entity.Quest;
import com.luckspinquest.entity.User;
import com.luckspinquest.entity.UserQuest;
import com.luckspinquest.repository.QuestRepository;
import com.luckspinquest.repository.UserQuestRepository;
import com.luckspinquest.repository.UserRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class QuestService {

    private final QuestRepository questRepository;
    private final UserQuestRepository userQuestRepository;
    private final UserRepository userRepository;

    public QuestService(
            QuestRepository questRepository,
            UserQuestRepository userQuestRepository,
            UserRepository userRepository
    ) {
        this.questRepository = questRepository;
        this.userQuestRepository = userQuestRepository;
        this.userRepository = userRepository;
    }

    public List<QuestResponse> getActiveQuests() {

        return questRepository
                .findByQuestStatusOrderByQuestNameAsc("ACTIVE")
                .stream()
                .map(this::toQuestResponse)
                .toList();
    }

    public List<QuestResponse> getQuestsByType(
            String questType
    ) {

        return questRepository
                .findByQuestType(questType)
                .stream()
                .filter(quest ->
                        "ACTIVE".equalsIgnoreCase(
                                quest.getQuestStatus()
                        )
                )
                .map(this::toQuestResponse)
                .toList();
    }

    public QuestResponse getQuest(Long questId) {

        Quest quest = questRepository
                .findById(questId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Quest not found"
                        )
                );

        return toQuestResponse(quest);
    }

    public List<UserQuestResponse> getUserQuestProgress(
            String username
    ) {

        User user = getUser(username);

        return userQuestRepository
                .findByUserUserIdOrderByStartedAtDesc(
                        user.getUserId()
                )
                .stream()
                .map(this::toUserQuestResponse)
                .toList();
    }

    public UserQuestResponse getSpecificQuestProgress(
            String username,
            Long questId
    ) {

        User user = getUser(username);

        List<UserQuest> userQuests =
                userQuestRepository
                        .findByUserUserIdAndQuestQuestId(
                                user.getUserId(),
                                questId
                        );

        if (userQuests.isEmpty()) {
            throw new IllegalArgumentException(
                    "Quest progress not found"
            );
        }

        return toUserQuestResponse(
                userQuests.get(0)
        );
    }

    private User getUser(String username) {

        return userRepository
                .findByUserUsername(username)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "User not found"
                        )
                );
    }

    private QuestResponse toQuestResponse(
            Quest quest
    ) {

        return new QuestResponse(
                quest.getQuestId(),
                quest.getQuestCode(),
                quest.getQuestName(),
                quest.getQuestDescription(),
                quest.getQuestType(),
                quest.getTargetValue(),
                quest.getCoinReward(),
                null,
                quest.getQuestStatus(),
                quest.getStartAt(),
                quest.getEndAt()
        );
    }

    private UserQuestResponse toUserQuestResponse(
            UserQuest userQuest
    ) {

        return new UserQuestResponse(
                userQuest.getUserQuestId(),
                userQuest.getUser().getUserId(),
                userQuest.getQuest().getQuestId(),
                userQuest.getProgressValue(),
                userQuest.getTargetValue(),
                userQuest.getQuestStatus(),
                userQuest.getStartedAt(),
                userQuest.getCompletedAt(),
                userQuest.getClaimedAt()
        );
    }
}
