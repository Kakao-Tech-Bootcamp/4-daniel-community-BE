package com.daniel.community.global.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private final SecretKey secretKey;
    private final long accessTokenExpiration;

    public JwtTokenProvider(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.access-token-expiration}") long accessTokenExpiration
    ) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.accessTokenExpiration = accessTokenExpiration;
    }

    // 로그인 성공 시 access token을 생성
    public String createAccessToken(Long userId) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + accessTokenExpiration);

        return Jwts.builder()
                // 작성자 판단 기준을 user_id로 설정
                .subject(String.valueOf(userId))
                // 토큰 발급시간
                .issuedAt(now)
                // 토큰 만료시간
                .expiration(expiration)
                .signWith(secretKey)
                .compact();
    }

    // 현재 로그인한 사용자를 판단하는 메서드
    public Long getUserId(String token) {
        Claims claims = parseClaims(token);
        return Long.parseLong(claims.getSubject());
    }

    // 토큰이 정상인지 확인하는 메서드
    public boolean validateToken(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (Exception exception) {
            return false;
        }
    }

    // 실제 JWT를 해석하는 메서드
    private Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}