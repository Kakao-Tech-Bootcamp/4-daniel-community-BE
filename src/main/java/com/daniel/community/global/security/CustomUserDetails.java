package com.daniel.community.global.security;

import com.daniel.community.user.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class CustomUserDetails implements UserDetails {

    private final User user;

    public CustomUserDetails(User user) {
        this.user = user;
    }

    public Long getUserId() {
        return user.getUserId();
    }

    public String getNickname() {
        return user.getNickname();
    }

    public String getProfileImage() {
        return user.getProfileImage();
    }

    public User getUser() {
        return user;
    }

    // 권한 목록을 반환하는 메서드
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    // 비밀번호 비교시 사용하는 메서드
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }
}