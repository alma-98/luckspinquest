package com.luckspinquest.service;

import com.luckspinquest.dto.user.UpdateUserRequest;
import com.luckspinquest.dto.user.UserResponse;
import com.luckspinquest.entity.User;
import com.luckspinquest.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional(readOnly = true)
    public UserResponse getCurrentUser() {
        return UserResponse.from(getAuthenticatedUser());
    }

    public UserResponse updateCurrentUser(UpdateUserRequest request) {
        User user = getAuthenticatedUser();

        if (request.getName() != null && !request.getName().isBlank()) {
            user.setUserName(request.getName().trim());
        }

        if (request.getEmail() != null && !request.getEmail().isBlank()) {
            String email = request.getEmail().trim();

            if (!email.equalsIgnoreCase(user.getUserEmail())
                    && userRepository.findByUserEmail(email).isPresent()) {
                throw new IllegalArgumentException("Email already exists");
            }

            user.setUserEmail(email);
            user.setEmailVerified(false);
        }

        if (request.getPhone() != null) {
            String phone = request.getPhone().trim();

            if (!phone.isEmpty()
                    && !phone.equals(user.getUserPhone())
                    && userRepository.findAll().stream()
                    .anyMatch(u -> phone.equals(u.getUserPhone())
                            && !u.getUserId().equals(user.getUserId()))) {
                throw new IllegalArgumentException("Phone already exists");
            }

            user.setUserPhone(phone.isEmpty() ? null : phone);
            user.setPhoneVerified(false);
        }

        return UserResponse.from(userRepository.save(user));
    }

    public void deleteCurrentUser() {
        User user = getAuthenticatedUser();
        user.setUserStatus("DELETED");
        userRepository.save(user);
    }

    private User getAuthenticatedUser() {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null
                || !authentication.isAuthenticated()
                || authentication.getName() == null
                || "anonymousUser".equals(authentication.getName())) {
            throw new IllegalStateException("User is not authenticated");
        }

        return userRepository.findByUserUsername(authentication.getName())
                .orElseThrow(() -> new IllegalStateException("Authenticated user not found"));
    }
}
