package com.example.cafe.service;

import com.example.cafe.config.JwtUtil;
import com.example.cafe.domain.User;
import com.example.cafe.dto.UserDto;
import com.example.cafe.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service // 이 클래스가 비즈니스 로직을 담당하는 서비스 계층의 컴포넌트임을 나타냅니다.
@RequiredArgsConstructor // final 필드에 대한 생성자를 자동으로 만들어 의존성을 주입합니다.
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder; // SecurityConfig에 Bean으로 등록한 객체가 주입됩니다.
    private final JwtUtil jwtUtil; // JwtUtil 의존성 주입

    @Transactional // 이 메서드 내의 모든 DB 작업이 하나의 트랜잭션으로 처리됩니다.
    public User signup(UserDto userDto) {
        // 사용자 이름 중복 검사
        if (userRepository.findByUsername(userDto.getUsername()).isPresent()) {
            throw new IllegalArgumentException("이미 가입된 사용자입니다.");
        }

        // DTO를 Entity로 변환
        User user = new User();
        user.setUsername(userDto.getUsername());
        // 비밀번호는 반드시 암호화하여 저장해야 합니다.
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        // 데이터베이스에 저장
        return userRepository.save(user);
    }

    // 로그인 메서드 추가
    @Transactional(readOnly = true) // 읽기 전용 트랜잭션
    public String login(UserDto userDto) {
        // 1. 아이디(username) 확인
        User user = userRepository.findByUsername(userDto.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("가입되지 않은 사용자입니다."));

        // 2. 비밀번호 확인
        if (!passwordEncoder.matches(userDto.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("비밀번호가 일치하지 않습니다.");
        }

        // 3. 인증 성공 시 JWT 생성 및 반환
        return jwtUtil.createToken(user.getUsername());
    }
}