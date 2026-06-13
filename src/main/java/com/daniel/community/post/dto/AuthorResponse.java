package com.daniel.community.post.dto;

import com.daniel.community.user.entity.User;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AuthorResponse {

    @JsonProperty("user_id")
    private Long userId;

    private String nickname;

    @JsonProperty("profile_image")
    private String profileImage;

    public AuthorResponse(User user) {
        this.userId = user.getUserId();
        this.nickname = user.getNickname();
        this.profileImage = user.getProfileImage();
    }

    public Long getUserId() {
        return userId;
    }

    public String getNickname() {
        return nickname;
    }

    public String getProfileImage() {
        return profileImage;
    }
}