package com.luckspinquest.dto.profile;

import java.time.LocalDate;

public record UpdateProfileRequest(
        String displayName,
        String gender,
        LocalDate birthDate,
        String country,
        String city,
        String language,
        String timezone
) {
}
