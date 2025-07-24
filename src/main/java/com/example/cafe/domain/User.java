package com.example.cafe.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity // 이 클래스가 데이터베이스 테이블과 매핑되는 클래스임을 나타냅니다.
@Table(name = "users") // 데이터베이스에 생성될 테이블의 이름을 'users'로 지정합니다.
@Getter
@Setter
@NoArgsConstructor // JPA는 기본 생성자를 필요로 하므로, Lombok을 통해 자동으로 생성합니다.
public class User {

    @Id // 이 필드가 테이블의 기본 키(Primary Key)임을 나타냅니다.
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 기본 키 생성을 데이터베이스에 위임합니다(예: AUTO_INCREMENT).
    private Long id;

    @Column(nullable = false, unique = true) // 'nullable=false'는 NOT NULL 제약조건, 'unique=true'는 UNIQUE 제약조건을 의미합니다.
    private String username;

    @Column(nullable = false)
    private String password;
}