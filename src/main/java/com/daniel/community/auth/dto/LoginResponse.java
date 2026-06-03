package com.daniel.community.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LoginResponse {

    @JsonProperty("user_id")
    private Long userId;

    @JsonProperty("access_token")
    private String accessToken;

    public LoginResponse(Long userId, String accessToken) {
        this.userId = userId;
        this.accessToken = accessToken;
    }

    public Long getUserId() {
        return userId;
    }

    public String getAccessToken() {
        return accessToken;
    }
}