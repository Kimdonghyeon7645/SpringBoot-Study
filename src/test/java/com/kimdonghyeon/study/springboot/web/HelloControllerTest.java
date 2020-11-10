package com.kimdonghyeon.study.springboot.web;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
/*
테스트 진행시 JUnit 내장 실행자와는 다른 실행자 실행 (여기선 SpringRunner 라는 스프링 실행자 실행)
-> 스프링부트와 JUnit 사이에 연결자 역할을 함)
 */
@WebMvcTest(controllers = HelloController.class)    // 여러 스프링 테스트 어노테이션 중에 Web(Spring WVC)에 집중할 수 있는 어노테이션
public class HelloControllerTest {      // HelloControllerTest 줄 왼쪽의 화살표 클릭 (클래스 실행, ctrl + shift + f10) -> 테스트 실행됨

    @Autowired      // 스프링이 관리하는 빈을 주입 받음
    private MockMvc mvc;    // 웹 API 테스트에 사용 -> 이 클래스로 HTTP GEt, POST 등에 대한 API 테스트 가능

    @Test
    public void HelloResponseTest() throws Exception {
        String hello = "힘쎄고 강한 아침! 만일 내게 물어보면 나는 SpringBoot.";
        // String hello = "안 힘쎄고 강한 아침! 만일 내게 물어보면 나는 SpringBoot.";     // 이걸로 테스트하면 테스트 실패 확인 가능

        mvc.perform(get("/hello"))      // MockMvc 를 통해 /hello 주소로 GET 요청을 보냄 (체이닝 지원 -> 검증 기능 여러개 같이 사용 가능)
                .andExpect(status().isOk())     // mvc.perform 결과를 검증 (status가 OK(200) 인지)
                .andExpect(content().string(hello));     // mvc.perform 결과를 검증 (content가 hello 변수 값과 같은지)
    }
}
