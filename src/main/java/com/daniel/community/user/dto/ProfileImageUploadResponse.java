package com.daniel.community.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ProfileImageUploadResponse {

    @JsonProperty("profile_image")
    private String profileImage;

    public ProfileImageUploadResponse(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getProfileImage() {
        return profileImage;
    }
}