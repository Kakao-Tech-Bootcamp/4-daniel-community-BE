package com.daniel.community.user.dto;

import com.daniel.community.user.entity.User;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UserInfoResponse {

    @JsonProperty("user_id")
    private Long userId;

    private String email;

    private String nickname;

    @JsonProperty("profile_image")
    private String profileImage;

    // 비밀번호는 응답에 미포함
    public UserInfoResponse(User user) {
        this.userId = user.getUserId();
        this.email = user.getEmail();
        this.nickname = user.getNickname();
        this.profileImage = user.getProfileImage();
    }

    public Long getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public String getNickname() {
        return nickname;
    }

    public String getProfileImage() {
        return profileImage;
    }
}