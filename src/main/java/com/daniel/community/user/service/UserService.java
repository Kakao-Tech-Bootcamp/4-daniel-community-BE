package com.daniel.community.user.service;

import com.daniel.community.user.dto.SignupRequest;
import com.daniel.community.user.dto.SignupResponse;
import com.daniel.community.user.entity.User;
import com.daniel.community.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    // 사용자 정보를 데이터베이스에 저장하거나 조회하기 위한 필드
    private final UserRepository userRepository;
    // 비밀번호를 암호화하기 위한 필드
    private final PasswordEncoder passwordEncoder;

    public UserService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public SignupResponse signup(SignupRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("email_duplicated");
        }

        if (userRepository.existsByNickname(request.getNickname())) {
            throw new IllegalArgumentException("nickname_duplicated");
        }

        String encodedPassword = passwordEncoder.encode(request.getPassword());

        // 암호화된 비밀번호로 유저 생성
        User user = new User(
                request.getEmail(),
                encodedPassword,
                request.getNickname(),
                request.getProfileImage()
        );

        User savedUser = userRepository.save(user);

        return new SignupResponse(savedUser.getUserId());
    }

    @Transactional(readOnly = true)
    // 이메일 중복 확인
    public void checkEmail(String email) {
        if (email == null || email.isBlank()) {
            // 이메일이 비어있는 경우 예외처리
            throw new IllegalArgumentException("email_request");
        }

        if (userRepository.existsByEmail(email)) {
            // 이메일이 중복인 경우 예외처리
            throw new IllegalArgumentException("email_duplicated");
        }
    }

    @Transactional(readOnly = true)
    // 닉네임 중복 확인
    public void checkNickname(String nickname) {
        if (nickname == null || nickname.isBlank()) {
            throw new IllegalArgumentException("nickname_request");
        }

        if (userRepository.existsByNickname(nickname)) {
            throw new IllegalArgumentException("nickname_duplicated");
        }
    }
}