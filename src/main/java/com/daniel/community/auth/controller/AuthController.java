package com.daniel.community.auth.controller;

import com.daniel.community.global.response.ApiResponse;
import com.daniel.community.auth.dto.LoginRequest;
import com.daniel.community.auth.dto.LoginResponse;
import com.daniel.community.auth.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    // 로그인 메서드
    @PostMapping("/users/login")
    public ResponseEntity<ApiResponse> login(@Valid @RequestBody LoginRequest request) {
        try {
            // 로그인 성공시 AuthService에서 LohinResponse를 받음
            LoginResponse response = authService.login(request);
            return ResponseEntity.ok(ApiResponse.success("login_success", response));
        } catch (IllegalArgumentException exception) {
            return ResponseEntity
                    // 비밀번호를 틀리거나 이메일이 존재하지 않으면 예외처리
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error(exception.getMessage()));
        }
    }

    // 로그아웃은 토큰삭제 방식으로 별도의 로직이 없음
    @DeleteMapping("/users/logout")
    public ResponseEntity<ApiResponse> logout() {
        return ResponseEntity.ok(ApiResponse.success("logout_success"));
    }
}