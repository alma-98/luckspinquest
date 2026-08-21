package com.luckspinquest.controller;

import com.luckspinquest.dto.profile.ChangePasswordRequest;
import com.luckspinquest.dto.profile.UpdateAvatarRequest;
import com.luckspinquest.dto.profile.UpdateProfileRequest;
import com.luckspinquest.service.ProfileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/profile")
public class ProfileController {

    private final ProfileService profileService;

    public ProfileController(
            ProfileService profileService
    ) {
        this.profileService = profileService;
    }

    @GetMapping
    public ResponseEntity<?> getProfile() {
        return ResponseEntity.ok(
                profileService.getProfile()
        );
    }

    @PutMapping
    public ResponseEntity<?> updateProfile(
            @RequestBody UpdateProfileRequest request
    ) {
        return ResponseEntity.ok(
                profileService.updateProfile(request)
        );
    }

    @PutMapping("/avatar")
    public ResponseEntity<?> updateAvatar(
            @RequestBody UpdateAvatarRequest request
    ) {
        return ResponseEntity.ok(
                profileService.updateAvatar(request)
        );
    }

    @PutMapping("/password")
    public ResponseEntity<?> changePassword(
            @RequestBody ChangePasswordRequest request
    ) {
        return ResponseEntity.ok(
                profileService.changePassword(request)
        );
    }

    @GetMapping("/security")
    public ResponseEntity<?> getSecurityInformation() {
        return ResponseEntity.ok(
                profileService.getSecurityInformation()
        );
    }
}
