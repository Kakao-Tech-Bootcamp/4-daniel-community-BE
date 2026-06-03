package com.daniel.community.global.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final CustomUserDetailsService customUserDetailsService;

    public JwtAuthenticationFilter(
            JwtTokenProvider jwtTokenProvider,
            CustomUserDetailsService customUserDetailsService
    ) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        // 요청 헤더에서 토큰을 추출
        String token = getToken(request);

        // 유효한 토큰인지 확인
        if (token != null && jwtTokenProvider.validateToken(token)) {
            // 토큰에서 사용자 ID를 추출
            Long userId = jwtTokenProvider.getUserId(token);
            // 추출한 ID로 데이터베이스에서 사용자 조회
            CustomUserDetails userDetails =
                    (CustomUserDetails) customUserDetailsService.loadUserByUserId(userId);

            // 인증 객체를 생성
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            userDetails, // 인증된 사용자 정보
                            null, // 비밀번호
                            userDetails.getAuthorities() // 권한목록
                    );

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        // Controller로 요청을 넘김
        filterChain.doFilter(request, response);
    }

    private String getToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader == null) {
            return null;
        }

        if (!authorizationHeader.startsWith("Bearer ")) {
            return null;
        }

        // 실제 토큰만 추출
        return authorizationHeader.substring(7);
    }
}