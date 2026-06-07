package com.daniel.community.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

public class ProfileImageUploadRequest {

    @NotBlank
    @JsonProperty("profile_image_name")
    private String profileImageName;

    @NotBlank
    @JsonProperty("profile_image_data")
    private String profileImageData;

    public String getProfileImageName() {
        return profileImageName;
    }

    public String getProfileImageData() {
        return profileImageData;
    }
}