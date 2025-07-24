package com.example.cafe.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy; // 세션 정책 설정을 위해 임포트
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // CSRF 보호 비활성화

                // ✅ 아래 두 줄이 추가/수정되었습니다.
                .httpBasic(httpBasic -> httpBasic.disable()) // HTTP Basic 인증 비활성화
                .formLogin(formLogin -> formLogin.disable()) // Form 기반 로그인 비활성화

                // REST API 서버이므로 세션을 사용하지 않도록 설정 (STATELESS)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/signup", "/api/login", "/h2-console/**").permitAll()
                        .anyRequest().authenticated()
                )
                .headers((headers) -> headers
                        .frameOptions((frameOptions) -> frameOptions.sameOrigin())
                );

        return http.build();
    }
}
