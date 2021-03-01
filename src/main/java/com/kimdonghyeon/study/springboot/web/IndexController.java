package com.kimdonghyeon.study.springboot.web;

import com.kimdonghyeon.study.springboot.service.posts.PostsService;
import com.kimdonghyeon.study.springboot.web.dto.PostsResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@RequiredArgsConstructor
@Controller
public class IndexController {

    private final PostsService postsService;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("posts", postsService.findAllDesc());
        /*
         서버 텀플릿 엔진(여기선 머스테치)에서 사용 가능한 객체를, Model 객체에 저장 가능
         여기선 postsService.findAllDesc() 의 반환 값을 posts 라는 이름으로 index.mustache 에 전달
        */
        return "index";
        /*
         머스테치 스타터 덕분에 컨트롤러에서 문자열 반환시, 앞의 경로(src/main/resources/templates) + 뒤의 파일 확장자(.mustache)는 자동으로 지정
         ViewResolver(URL 요청의 결과를 전달할 타입, 값을 지정하는 관리자 같은 느낌)가 위같이 자동으로 지정된 값으로 처리함
        */
    }

    @GetMapping("/posts/save")
    public String postsSave() {
        return "posts-save";
    }

    @GetMapping("/posts/update/{id}")
    public String postsUpdate(@PathVariable Long id, Model model) {
        PostsResponseDto dto = postsService.findById(id);
        model.addAttribute("post", dto);

        return "posts-update";
    }
}
