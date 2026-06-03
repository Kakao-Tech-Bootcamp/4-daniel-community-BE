package com.daniel.community.auth.service;

import com.daniel.community.auth.dto.LoginRequest;
import com.daniel.community.auth.dto.LoginResponse;
import com.daniel.community.user.entity.User;
import com.daniel.community.user.repository.UserRepository;
import com.daniel.community.global.security.JwtTokenProvider;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtTokenProvider jwtTokenProvider
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest request) {
        // 이메일로 사용자를 탐색
        User user = userRepository.findByEmail(request.getEmail())
                // 사용자가 없는 경우 예외처리
                .orElseThrow(() -> new IllegalArgumentException("login_failed"));

        // 입력한 비밀번호를 암호화된 비밀번호와 비교
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("login_failed");
        }

        // 검증을 마치면 JWT를 생성
        String accessToken = jwtTokenProvider.createAccessToken(user.getUserId());

        return new LoginResponse(user.getUserId(), accessToken);
    }
}