package com.daniel.community.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AuthorResponse {

    private String nickname;

    @JsonProperty("profile_image")
    private String profileImage;

    public AuthorResponse(String nickname, String profileImage) {
        this.nickname = nickname;
        this.profileImage = profileImage;
    }

    public String getNickname() {
        return nickname;
    }

    public String getProfileImage() {
        return profileImage;
    }
}
