package com.kimdonghyeon.study.springboot.web;

import com.kimdonghyeon.study.springboot.web.dto.HelloResponseDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController     // 컨트롤러를 JSON 타입 반환을 하도록 만들어 줌

public class HelloController {

    @GetMapping("/hello")       // Get(Http method) 요청을 받을수 있는 API 만들어 줌
    public String hello() {
        return "힘쎄고 강한 아침! 만일 내게 물어보면 나는 SpringBoot.";
    }

    // 롬복으로 처리되는 API 코드 추가
    @GetMapping("/hello/dto")
    public HelloResponseDto helloDto(@RequestParam("name") String name, @RequestParam("amount") int amount) {
        /*
        @RequestParam
        외부에서 API한테 넘긴 파라미터를 가져오는 어노테이션
        ```@RequestParam(전달받은_파라미터의_이름) 변수타입 전달받은_파라미터를_저장할_변수```
         */
        return new HelloResponseDto(name, amount);
    }
}
