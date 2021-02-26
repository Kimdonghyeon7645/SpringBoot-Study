package com.kimdonghyeon.study.springboot.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {

    @GetMapping("/")
    public String index() {
        return "index";
        // 머스테치 스타터 덕분에 컨트롤러에서 문자열 반환시, 앞의 경로(src/main/resources/templates) + 뒤의 파일 확장자(.mustache)는 자동으로 지정
        // ViewResolver(URL 요청의 결과를 전달할 타입, 값을 지정하는 관리자 같은 느낌)가 위같이 자동으로 지정된 값으로 처리함
    }
}
