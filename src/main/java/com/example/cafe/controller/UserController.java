package com.example.cafe.controller;

import com.example.cafe.dto.UserDto;
import com.example.cafe.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;

@RestController // 이 클래스가 REST API를 처리하는 컨트롤러임을 나타냅니다. (@Controller + @ResponseBody)
@RequestMapping("/api") // 이 컨트롤러의 모든 메서드는 '/api' 경로 하위에 매핑됩니다.
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // POST /api/signup 요청을 처리하는 메서드
    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody UserDto userDto) {
                try {
                    userService.signup(userDto);
                    // 성공 시 HTTP 201 Created 상태 코드와 메시지를 반환합니다.
                    return ResponseEntity.status(HttpStatus.CREATED).body("회원가입 성공");
                } catch (IllegalArgumentException e) {
                    // 실패 시(예: 사용자 이름 중복) HTTP 409 Conflict 상태 코드와 에러 메시지를 반환합니다.
                    return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    // 로그인 요청을 처리하는 POST 엔드포인트
    // URL: POST /api/login
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserDto userDto) {
        try {
            String token = userService.login(userDto);
            // 성공 시 HTTP 200 OK 상태 코드와 JWT 토큰을 반환
            return ResponseEntity.ok(token);
        } catch (UsernameNotFoundException | BadCredentialsException e) {
            // 실패 시(아이디 또는 비밀번호 오류) HTTP 401 Unauthorized 상태 코드와 에러 메시지 반환
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @GetMapping("/profile")
    public ResponseEntity<String> getProfile() {
        // SecurityContext에서 인증 정보를 가져와 사용자 이름을 추출합니다.
        // 이 로직은 JWT 필터가 정상적으로 동작해야만 실행됩니다.
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        return ResponseEntity.ok("Profile of: " + username);
    }
}
