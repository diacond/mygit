package com.example.cafe.dto;

import lombok.Getter;
import lombok.Setter;

// 회원가입 요청 시 필요한 데이터(username, password)를 담는 객체입니다.
@Getter
@Setter
public class UserDto {
    private String username;
    private String password;
}

