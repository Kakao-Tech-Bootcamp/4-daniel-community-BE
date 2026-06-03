package com.daniel.community.global.config;

import com.daniel.community.global.security.CustomUserDetailsService;
import com.daniel.community.global.security.JwtAuthenticationFilter;
import com.daniel.community.global.security.JwtTokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;
    private final CustomUserDetailsService customUserDetailsService;

    public SecurityConfig(
            JwtTokenProvider jwtTokenProvider,
            CustomUserDetailsService customUserDetailsService
    ) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Bean
    // 어떤 요청을 허용할지와 어떤 요청에 로그인이 필요한지 설정
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        JwtAuthenticationFilter jwtAuthenticationFilter =
                new JwtAuthenticationFilter(jwtTokenProvider, customUserDetailsService);

        return http
                .csrf(AbstractHttpConfigurer::disable)
                // JWT 인증은 세션을 사용하지 않기 때문에 STATELESS 로 설정
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth -> auth
                        // 아래의 API는 누구나 접근 가능
                        .requestMatchers(HttpMethod.GET, "/posts").permitAll()
                        .requestMatchers(HttpMethod.GET, "/posts/{postId}").permitAll()
                        .requestMatchers("/users/signup").permitAll()
                        .requestMatchers("/users/login").permitAll()
                        .requestMatchers("/users/emails/**").permitAll()
                        .requestMatchers("/users/nicknames/**").permitAll()
                        // 그 외 API는 로그인해야 접근 가능
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    // 비밀번호 암호화를 위한 Bean 등록
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}