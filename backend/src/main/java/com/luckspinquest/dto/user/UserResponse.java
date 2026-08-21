package com.luckspinquest.dto.user;

import com.luckspinquest.entity.User;

import java.time.LocalDateTime;

public class UserResponse {

    private Long userId;
    private String username;
    private String name;
    private String email;
    private String phone;
    private String status;
    private boolean emailVerified;
    private boolean phoneVerified;
    private LocalDateTime lastLoginAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static UserResponse from(User user) {
        UserResponse response = new UserResponse();

        response.userId = user.getUserId();
        response.username = user.getUserUsername();
        response.name = user.getUserName();
        response.email = user.getUserEmail();
        response.phone = user.getUserPhone();
        response.status = user.getUserStatus();
        response.emailVerified = user.getEmailVerified();
        response.phoneVerified = user.getPhoneVerified();
        response.lastLoginAt = user.getLastLoginAt();
        response.createdAt = user.getCreatedAt();
        response.updatedAt = user.getUpdatedAt();

        return response;
    }

    public Long getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getStatus() {
        return status;
    }

    public boolean isEmailVerified() {
        return emailVerified;
    }

    public boolean isPhoneVerified() {
        return phoneVerified;
    }

    public LocalDateTime getLastLoginAt() {
        return lastLoginAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
}
