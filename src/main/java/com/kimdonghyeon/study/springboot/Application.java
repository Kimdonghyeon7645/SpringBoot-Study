package com.kimdonghyeon.study.springboot;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


@EnableJpaAuditing  // JPA의 Auditing을 활성화
@SpringBootApplication
/*
@SpringBootApplication
스프링부트의 자동 설정, 스프링 빈 읽기, 생성을 모두 자동으로 설정
이 위치부터 설정을 읽음 -> 항상 이 클래스는 프로젝트 최상단에 위치해야함!
 */
public class Application {
    public static void main(String[] args) {    // main 메소드 왼쪽 화살표 클릭 (메소드 실행) -> 수동으로 서버(WAS) 실행
        SpringApplication.run(Application.class, args);     // 내장 WAS(웹 어플리케이션 서버) 실행
    }
}
