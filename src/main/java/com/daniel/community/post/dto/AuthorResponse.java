package com.daniel.community.post.dto;

import com.daniel.community.user.entity.User;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AuthorResponse {

    private String nickname;

    @JsonProperty("profile_image")
    private String profileImage;

    public AuthorResponse(User user) {
        this.nickname = user.getNickname();
        this.profileImage = user.getProfileImage();
    }

    public String getNickname() {
        return nickname;
    }

    public String getProfileImage() {
        return profileImage;
    }
}