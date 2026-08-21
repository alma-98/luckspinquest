package com.luckspinquest.controller;

import com.luckspinquest.dto.user.UpdateUserRequest;
import com.luckspinquest.dto.user.UserResponse;
import com.luckspinquest.service.UserService;
import com.luckspinquest.service.UserStatisticsService;
import com.luckspinquest.service.UserActivityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;
    private final UserStatisticsService userStatisticsService;
    private final UserActivityService userActivityService;

    public UserController(
            UserService userService,
            UserStatisticsService userStatisticsService,
            UserActivityService userActivityService
    ) {
        this.userService = userService;
        this.userStatisticsService = userStatisticsService;
        this.userActivityService = userActivityService;
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser() {
        return ResponseEntity.ok(userService.getCurrentUser());
    }

    @PutMapping("/me")
    public ResponseEntity<UserResponse> updateCurrentUser(
            @RequestBody UpdateUserRequest request) {
        return ResponseEntity.ok(userService.updateCurrentUser(request));
    }

    @DeleteMapping("/me")
    public ResponseEntity<?> deleteCurrentUser() {
        userService.deleteCurrentUser();

        return ResponseEntity.ok(
                Map.of(
                        "message", "Current user deleted",
                        "status", "DELETED"
                )
        );
    }

    @GetMapping("/me/activity")
    public ResponseEntity<?> getUserActivity() {
        return ResponseEntity.ok(
                userActivityService.getActivity()
        );
    }

    @GetMapping("/me/statistics")
    public ResponseEntity<?> getUserStatistics() {
        return ResponseEntity.ok(
                userStatisticsService.getStatistics()
        );
    }
}
