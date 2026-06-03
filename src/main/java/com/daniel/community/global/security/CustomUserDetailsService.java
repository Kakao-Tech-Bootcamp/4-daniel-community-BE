package com.daniel.community.global.security;

import com.daniel.community.user.entity.User;
import com.daniel.community.user.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    // 데이터베이스 접근을 위해 userRepository를 주입
    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) {
        // 이메일로 사용자를 찾고 없으면 예외처리
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("login_failed"));

        // 사용자를 찾으면 CustomUserDetails로 감싸서 반환
        return new CustomUserDetails(user);
    }

    public UserDetails loadUserByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("unauthorized"));

        return new CustomUserDetails(user);
    }
}