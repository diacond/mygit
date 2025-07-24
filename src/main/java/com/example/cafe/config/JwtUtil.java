// src/main/java/com/example/cafe/config/JwtUtil.java

package com.example.cafe.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtUtil {

    // JWT의 비밀 키. application.properties에서 값을 가져옵니다.
    // **중요**: 이 키는 외부에 노출되면 안 됩니다. 실제 프로젝트에서는 환경 변수 등을 사용하세요.
    @Value("${jwt.secret}")
    private String secretKey;

    // JWT를 생성하는 메서드
    public String createToken(String username) {
        // JWT에 포함될 정보(Claims) 설정
        Claims claims = Jwts.claims();
        claims.put("username", username);

        // JWT 서명에 사용할 키 생성
        Key key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        long expirationTime = 1000L * 60 * 60; // 토큰 만료 시간: 1시간

        return Jwts.builder()
                .setClaims(claims) // 정보 설정
                .setIssuedAt(new Date(System.currentTimeMillis())) // 토큰 발행 시간
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime)) // 토큰 만료 시간
                .signWith(key, SignatureAlgorithm.HS256) // 서명에 사용할 키와 알고리즘 설정
                .compact(); // JWT 생성
    }

    // 토큰에서 사용자 이름 추출
    public String getUsernameFromToken(String token) {
        Key key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("username", String.class);
    }

    // 토큰 유효성 검증
    public boolean validateToken(String token) {
        try {
            Key key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SignatureException e) {
            log.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }
}
