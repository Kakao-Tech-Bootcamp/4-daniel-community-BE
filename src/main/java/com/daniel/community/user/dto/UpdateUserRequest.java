package com.daniel.community.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UpdateUserRequest {

    private String nickname;

    @JsonProperty("profile_image")
    private String profileImage;

    public String getNickname() {
        return nickname;
    }

    public String getProfileImage() {
        return profileImage;
    }
}