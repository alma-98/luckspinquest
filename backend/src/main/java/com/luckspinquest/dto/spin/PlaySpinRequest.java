package com.luckspinquest.dto.spin;

import jakarta.validation.constraints.NotBlank;

public class PlaySpinRequest {

    @NotBlank
    private String spinId;

    public String getSpinId() {
        return spinId;
    }

    public void setSpinId(String spinId) {
        this.spinId = spinId;
    }
}
