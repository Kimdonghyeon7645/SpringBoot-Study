package com.kimdonghyeon.study.springboot.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController     // 컨트롤러를 JSON 타입 반환을 하도록 만들어 줌

public class HelloController {

    @GetMapping("/hello")       // Get(Http method) 요청을 받을수 있는 API 만들어 줌
    public String hello() {
        return "힘쎄고 강한 아침! 만일 내게 물어보면 나는 SpringBoot.";
    }
}
