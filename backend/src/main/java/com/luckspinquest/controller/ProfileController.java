package com.luckspinquest.controller;

import com.luckspinquest.dto.profile.UpdateProfileRequest;
import com.luckspinquest.dto.profile.ChangePasswordRequest;
import com.luckspinquest.dto.profile.UpdateAvatarRequest;
import com.luckspinquest.entity.User;
import com.luckspinquest.entity.UserProfile;
import com.luckspinquest.repository.UserProfileRepository;
import com.luckspinquest.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/profile")
public class ProfileController {

    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public ProfileController(
            UserRepository userRepository,
            UserProfileRepository userProfileRepository
    ) {
        this.userRepository = userRepository;
        this.userProfileRepository = userProfileRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @GetMapping
    public ResponseEntity<?> getProfile() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null
                || !authentication.isAuthenticated()
                || authentication.getName() == null
                || "anonymousUser".equals(authentication.getName())) {

            return ResponseEntity.status(401).body(
                    Map.of("message", "Authentication required")
            );
        }

        User user = userRepository
                .findByUserUsername(authentication.getName())
                .orElse(null);

        if (user == null) {
            return ResponseEntity.status(404).body(
                    Map.of("message", "User not found")
            );
        }

        UserProfile profile = userProfileRepository
                .findByUserUserId(user.getUserId())
                .orElse(null);

        if (profile == null) {
            return ResponseEntity.status(404).body(
                    Map.of(
                            "message", "Profile not found",
                            "userId", user.getUserId()
                    )
            );
        }

        return ResponseEntity.ok(toResponse(user, profile));
    }

    @PutMapping
    public ResponseEntity<?> updateProfile(
            @RequestBody UpdateProfileRequest request
    ) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null
                || !authentication.isAuthenticated()
                || authentication.getName() == null
                || "anonymousUser".equals(authentication.getName())) {

            return ResponseEntity.status(401).body(
                    Map.of("message", "Authentication required")
            );
        }

        User user = userRepository
                .findByUserUsername(authentication.getName())
                .orElse(null);

        if (user == null) {
            return ResponseEntity.status(404).body(
                    Map.of("message", "User not found")
            );
        }

        UserProfile profile = userProfileRepository
                .findByUserUserId(user.getUserId())
                .orElseGet(() -> {
                    UserProfile newProfile = new UserProfile();
                    newProfile.setUser(user);
                    newProfile.setCreatedAt(LocalDateTime.now());
                    return newProfile;
                });

        if (request == null) {
            return ResponseEntity.badRequest().body(
                    Map.of("message", "Request body is required")
            );
        }

        if (request.displayName() != null) {
            profile.setProfileDisplayName(request.displayName());
        }

        if (request.gender() != null) {
            profile.setProfileGender(request.gender());
        }

        if (request.birthDate() != null) {
            profile.setProfileBirthDate(request.birthDate());
        }

        if (request.country() != null) {
            profile.setProfileCountry(request.country());
        }

        if (request.city() != null) {
            profile.setProfileCity(request.city());
        }

        if (request.language() != null) {
            profile.setProfileLanguage(request.language());
        }

        if (request.timezone() != null) {
            profile.setProfileTimezone(request.timezone());
        }

        LocalDateTime now = LocalDateTime.now();

        if (profile.getCreatedAt() == null) {
            profile.setCreatedAt(now);
        }

        profile.setUpdatedAt(now);

        UserProfile saved = userProfileRepository.save(profile);

        return ResponseEntity.ok(toResponse(user, saved));
    }

    @PutMapping("/avatar")
    public ResponseEntity<?> updateAvatar(
            @RequestBody UpdateAvatarRequest request
    ) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null
                || !authentication.isAuthenticated()
                || authentication.getName() == null
                || "anonymousUser".equals(authentication.getName())) {

            return ResponseEntity.status(401).body(
                    Map.of("message", "Authentication required")
            );
        }

        User user = userRepository
                .findByUserUsername(authentication.getName())
                .orElse(null);

        if (user == null) {
            return ResponseEntity.status(404).body(
                    Map.of("message", "User not found")
            );
        }

        if (request == null
                || request.avatarUrl() == null
                || request.avatarUrl().isBlank()) {

            return ResponseEntity.badRequest().body(
                    Map.of("message", "Avatar URL wajib diisi")
            );
        }

        UserProfile profile = userProfileRepository
                .findByUserUserId(user.getUserId())
                .orElse(null);

        if (profile == null) {
            return ResponseEntity.status(404).body(
                    Map.of(
                            "message", "Profile not found",
                            "userId", user.getUserId()
                    )
            );
        }

        profile.setProfileAvatarUrl(request.avatarUrl());

        LocalDateTime now = LocalDateTime.now();
        profile.setUpdatedAt(now);

        UserProfile saved = userProfileRepository.save(profile);

        return ResponseEntity.ok(toResponse(user, saved));
    }

    @PutMapping("/password")
    public ResponseEntity<?> changePassword(
            @RequestBody ChangePasswordRequest request
    ) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null
                || !authentication.isAuthenticated()
                || authentication.getName() == null
                || "anonymousUser".equals(authentication.getName())) {

            return ResponseEntity.status(401).body(
                    Map.of("message", "Authentication required")
            );
        }

        if (request == null
                || request.currentPassword() == null
                || request.currentPassword().isBlank()
                || request.newPassword() == null
                || request.newPassword().isBlank()) {

            return ResponseEntity.badRequest().body(
                    Map.of(
                            "message",
                            "Current password dan new password wajib diisi"
                    )
            );
        }

        if (request.newPassword().length() < 8) {
            return ResponseEntity.badRequest().body(
                    Map.of(
                            "message",
                            "Password baru minimal 8 karakter"
                    )
            );
        }

        if (request.currentPassword().equals(request.newPassword())) {
            return ResponseEntity.badRequest().body(
                    Map.of(
                            "message",
                            "Password baru harus berbeda dari password lama"
                    )
            );
        }

        User user = userRepository
                .findByUserUsername(authentication.getName())
                .orElse(null);

        if (user == null) {
            return ResponseEntity.status(404).body(
                    Map.of("message", "User not found")
            );
        }

        if (!passwordEncoder.matches(
                request.currentPassword(),
                user.getUserPasswordHash()
        )) {
            return ResponseEntity.status(400).body(
                    Map.of("message", "Password lama salah")
            );
        }

        user.setUserPasswordHash(
                passwordEncoder.encode(request.newPassword())
        );

        user.setUpdatedAt(LocalDateTime.now());

        userRepository.save(user);

        return ResponseEntity.ok(
                Map.of("message", "Password changed successfully")
        );
    }

    @GetMapping("/security")
    public ResponseEntity<?> getSecurityInformation() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null
                || !authentication.isAuthenticated()
                || authentication.getName() == null
                || "anonymousUser".equals(authentication.getName())) {

            return ResponseEntity.status(401).body(
                    Map.of("message", "Authentication required")
            );
        }

        User user = userRepository
                .findByUserUsername(authentication.getName())
                .orElse(null);

        if (user == null) {
            return ResponseEntity.status(404).body(
                    Map.of("message", "User not found")
            );
        }

        Map<String, Object> response = new LinkedHashMap<>();

        response.put("userId", user.getUserId());
        response.put("username", user.getUserUsername());
        response.put("status", user.getUserStatus());
        response.put("emailVerified", user.getEmailVerified());
        response.put("phoneVerified", user.getPhoneVerified());
        response.put("lastLoginAt", user.getLastLoginAt());
        response.put("createdAt", user.getCreatedAt());
        response.put("updatedAt", user.getUpdatedAt());

        return ResponseEntity.ok(response);
    }

    private Map<String, Object> toResponse(
            User user,
            UserProfile profile
    ) {

        Map<String, Object> response = new LinkedHashMap<>();

        response.put("profileId", profile.getProfileId());
        response.put("userId", user.getUserId());
        response.put("displayName", profile.getProfileDisplayName());
        response.put("avatarUrl", profile.getProfileAvatarUrl());
        response.put("gender", profile.getProfileGender());
        response.put("birthDate", profile.getProfileBirthDate());
        response.put("country", profile.getProfileCountry());
        response.put("city", profile.getProfileCity());
        response.put("language", profile.getProfileLanguage());
        response.put("timezone", profile.getProfileTimezone());
        response.put("createdAt", profile.getCreatedAt());
        response.put("updatedAt", profile.getUpdatedAt());

        return response;
    }
}
