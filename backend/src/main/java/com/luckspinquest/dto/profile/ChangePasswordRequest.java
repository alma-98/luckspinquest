package com.luckspinquest.dto.profile;

public record ChangePasswordRequest(
        String currentPassword,
        String newPassword
) {
}
