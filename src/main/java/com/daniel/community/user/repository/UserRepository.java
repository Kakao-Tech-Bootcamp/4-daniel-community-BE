package com.daniel.community.user.repository;

import com.daniel.community.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // 이메일로 사용자를 찾는 메서드
    Optional<User> findByEmail(String email);

    // 이메일 중복을 확인하는 메서드
    boolean existsByEmail(String email);

    // 닉네임 중복을 확인하는 메서드
    boolean existsByNickname(String nickname);
}