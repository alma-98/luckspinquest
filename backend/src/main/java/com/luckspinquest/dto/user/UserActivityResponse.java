package com.luckspinquest.dto.user;

import java.time.LocalDateTime;

public class UserActivityResponse {

    private String type;
    private Long referenceId;
    private String description;
    private LocalDateTime timestamp;

    public UserActivityResponse(
            String type,
            Long referenceId,
            String description,
            LocalDateTime timestamp
    ) {
        this.type = type;
        this.referenceId = referenceId;
        this.description = description;
        this.timestamp = timestamp;
    }

    public String getType() {
        return type;
    }

    public Long getReferenceId() {
        return referenceId;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
