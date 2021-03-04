package com.kimdonghyeon.study.springboot.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
    /*
     소셜 로그인으로 반환되는 값의 email을 통하여
     이미 생성된 사용자인지 처음 가입하는 사용자인지 판단하기 위한 메소드

     Optional<T> 란 : https://www.daleseo.com/java8-optional-after/
     */
}
