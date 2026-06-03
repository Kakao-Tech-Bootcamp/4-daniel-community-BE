package com.daniel.community.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

public class UpdatePasswordRequest {

    @NotBlank
    @JsonProperty("current_password")
    private String currentPassword;

    @NotBlank
    @JsonProperty("new_password")
    private String newPassword;

    public String getCurrentPassword() {
        return currentPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }
}