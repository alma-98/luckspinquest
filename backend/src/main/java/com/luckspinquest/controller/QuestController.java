package com.luckspinquest.controller;

import com.luckspinquest.dto.quest.QuestResponse;
import com.luckspinquest.dto.quest.UserQuestResponse;
import com.luckspinquest.service.QuestService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/quests")
public class QuestController {

    private final QuestService questService;

    public QuestController(QuestService questService) {
        this.questService = questService;
    }

    /**
     * GET /api/v1/quests
     *
     * Get all active quests.
     */
    @GetMapping
    public ResponseEntity<List<QuestResponse>> getQuests() {

        return ResponseEntity.ok(
                questService.getActiveQuests()
        );
    }

    /**
     * GET /api/v1/quests/daily
     *
     * Get active daily quests.
     */
    @GetMapping("/daily")
    public ResponseEntity<List<QuestResponse>> getDailyQuests() {

        return ResponseEntity.ok(
                questService.getQuestsByType("DAILY")
        );
    }

    /**
     * GET /api/v1/quests/weekly
     *
     * Get active weekly quests.
     */
    @GetMapping("/weekly")
    public ResponseEntity<List<QuestResponse>> getWeeklyQuests() {

        return ResponseEntity.ok(
                questService.getQuestsByType("WEEKLY")
        );
    }

    /**
     * GET /api/v1/quests/special
     *
     * Get active special quests.
     */
    @GetMapping("/special")
    public ResponseEntity<List<QuestResponse>> getSpecialQuests() {

        return ResponseEntity.ok(
                questService.getQuestsByType("SPECIAL")
        );
    }

    /**
     * GET /api/v1/quests/{id}
     *
     * Get quest detail.
     */
    @GetMapping("/{id}")
    public ResponseEntity<QuestResponse> getQuest(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                questService.getQuest(id)
        );
    }

    /**
     * GET /api/v1/quests/progress
     *
     * Get current user's quest progress.
     */
    @GetMapping("/progress")
    public ResponseEntity<List<UserQuestResponse>> getQuestProgress(
            Authentication authentication
    ) {

        return ResponseEntity.ok(
                questService.getUserQuestProgress(
                        authentication.getName()
                )
        );
    }

    /**
     * GET /api/v1/quests/{id}/progress
     *
     * Get current user's progress for one quest.
     */
    @GetMapping("/{id}/progress")
    public ResponseEntity<UserQuestResponse> getSpecificQuestProgress(
            @PathVariable Long id,
            Authentication authentication
    ) {

        return ResponseEntity.ok(
                questService.getSpecificQuestProgress(
                        authentication.getName(),
                        id
                )
        );
    }
}
