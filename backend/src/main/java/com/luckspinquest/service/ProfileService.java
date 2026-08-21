package com.luckspinquest.service;

import com.luckspinquest.dto.profile.ChangePasswordRequest;
import com.luckspinquest.dto.profile.UpdateAvatarRequest;
import com.luckspinquest.dto.profile.UpdateProfileRequest;
import com.luckspinquest.entity.User;
import com.luckspinquest.entity.UserProfile;
import com.luckspinquest.repository.UserProfileRepository;
import com.luckspinquest.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
@Transactional
public class ProfileService {

    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public ProfileService(
            UserRepository userRepository,
            UserProfileRepository userProfileRepository
    ) {
        this.userRepository = userRepository;
        this.userProfileRepository = userProfileRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @Transactional(readOnly = true)
    public Map<String, Object> getProfile() {
        User user = getAuthenticatedUser();

        UserProfile profile = userProfileRepository
                .findByUserUserId(user.getUserId())
                .orElseThrow(() ->
                        new IllegalStateException("Profile not found")
                );

        return toResponse(user, profile);
    }

    public Map<String, Object> updateProfile(
            UpdateProfileRequest request
    ) {
        if (request == null) {
            throw new IllegalArgumentException(
                    "Request body is required"
            );
        }

        User user = getAuthenticatedUser();

        UserProfile profile = userProfileRepository
                .findByUserUserId(user.getUserId())
                .orElseGet(() -> {
                    UserProfile newProfile = new UserProfile();
                    newProfile.setUser(user);
                    newProfile.setCreatedAt(LocalDateTime.now());
                    return newProfile;
                });

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

        return toResponse(user, saved);
    }

    public Map<String, Object> updateAvatar(
            UpdateAvatarRequest request
    ) {
        if (request == null
                || request.avatarUrl() == null
                || request.avatarUrl().isBlank()) {
            throw new IllegalArgumentException(
                    "Avatar URL wajib diisi"
            );
        }

        User user = getAuthenticatedUser();

        UserProfile profile = userProfileRepository
                .findByUserUserId(user.getUserId())
                .orElseThrow(() ->
                        new IllegalStateException(
                                "Profile not found"
                        )
                );

        profile.setProfileAvatarUrl(request.avatarUrl());
        profile.setUpdatedAt(LocalDateTime.now());

        UserProfile saved = userProfileRepository.save(profile);

        return toResponse(user, saved);
    }

    public Map<String, Object> changePassword(
            ChangePasswordRequest request
    ) {
        if (request == null
                || request.currentPassword() == null
                || request.currentPassword().isBlank()
                || request.newPassword() == null
                || request.newPassword().isBlank()) {
            throw new IllegalArgumentException(
                    "Current password dan new password wajib diisi"
            );
        }

        if (request.newPassword().length() < 8) {
            throw new IllegalArgumentException(
                    "Password baru minimal 8 karakter"
            );
        }

        if (request.currentPassword()
                .equals(request.newPassword())) {
            throw new IllegalArgumentException(
                    "Password baru harus berbeda dari password lama"
            );
        }

        User user = getAuthenticatedUser();

        if (!passwordEncoder.matches(
                request.currentPassword(),
                user.getUserPasswordHash()
        )) {
            throw new IllegalArgumentException(
                    "Password lama salah"
            );
        }

        user.setUserPasswordHash(
                passwordEncoder.encode(
                        request.newPassword()
                )
        );

        user.setUpdatedAt(LocalDateTime.now());

        userRepository.save(user);

        return Map.of(
                "message",
                "Password changed successfully"
        );
    }

    @Transactional(readOnly = true)
    public Map<String, Object> getSecurityInformation() {
        User user = getAuthenticatedUser();

        Map<String, Object> response =
                new LinkedHashMap<>();

        response.put("userId", user.getUserId());
        response.put("username", user.getUserUsername());
        response.put("status", user.getUserStatus());
        response.put("emailVerified", user.getEmailVerified());
        response.put("phoneVerified", user.getPhoneVerified());
        response.put("lastLoginAt", user.getLastLoginAt());
        response.put("createdAt", user.getCreatedAt());
        response.put("updatedAt", user.getUpdatedAt());

        return response;
    }

    private User getAuthenticatedUser() {
        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        if (authentication == null
                || !authentication.isAuthenticated()
                || authentication.getName() == null
                || "anonymousUser".equals(
                        authentication.getName()
                )) {
            throw new IllegalStateException(
                    "Authentication required"
            );
        }

        return userRepository
                .findByUserUsername(authentication.getName())
                .orElseThrow(() ->
                        new IllegalStateException(
                                "User not found"
                        )
                );
    }

    private Map<String, Object> toResponse(
            User user,
            UserProfile profile
    ) {
        Map<String, Object> response =
                new LinkedHashMap<>();

        response.put(
                "profileId",
                profile.getProfileId()
        );
        response.put("userId", user.getUserId());
        response.put(
                "displayName",
                profile.getProfileDisplayName()
        );
        response.put(
                "avatarUrl",
                profile.getProfileAvatarUrl()
        );
        response.put(
                "gender",
                profile.getProfileGender()
        );
        response.put(
                "birthDate",
                profile.getProfileBirthDate()
        );
        response.put(
                "country",
                profile.getProfileCountry()
        );
        response.put(
                "city",
                profile.getProfileCity()
        );
        response.put(
                "language",
                profile.getProfileLanguage()
        );
        response.put(
                "timezone",
                profile.getProfileTimezone()
        );
        response.put(
                "createdAt",
                profile.getCreatedAt()
        );
        response.put(
                "updatedAt",
                profile.getUpdatedAt()
        );

        return response;
    }
}
