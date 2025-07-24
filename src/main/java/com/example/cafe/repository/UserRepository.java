package com.example.cafe.repository;

import com.example.cafe.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

// JpaRepository<관리할 엔티티, 엔티티의 ID 타입>를 상속받으면 기본적인 CRUD 메서드가 자동으로 생성됩니다.
public interface UserRepository extends JpaRepository<User, Long> {

    // 'findByUsername'이라는 이름으로 메서드를 만들면 Spring Data JPA가 자동으로
    // 'SELECT * FROM users WHERE username = ?' 쿼리를 생성해 줍니다.
    // Optional<T>는 결과가 null일 수도 있음을 명시적으로 표현합니다.
    Optional<User> findByUsername(String username);
}
