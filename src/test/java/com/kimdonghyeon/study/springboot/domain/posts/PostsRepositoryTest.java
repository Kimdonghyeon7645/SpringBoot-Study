package com.kimdonghyeon.study.springboot.domain.posts;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class PostsRepositoryTest {

    @Autowired
    PostsRepository postsRepository;

    @After      // Junit 에서 단위 테스트가 끝날때 마다 수행되는 메소드를 지정  
    public void cleanup() {
        postsRepository.deleteAll();
    }

    @Test
    public void writeAndLoadPost() {
        //given
        String title = "테스트 게시글";
        String content = "테스트 본문";

        postsRepository.save(Posts.builder()    // postsRepository.save : 테이블 posts에서, 인자값에 id값이 있으면 update, 없으면 insert 쿼리 실행
                .title(title)
                .content(content)
                .author("dongdongYee@gmail.com")
                .build());

        //when
        List<Posts> postsList = postsRepository.findAll();      // findAll() : posts 테이블의 모든 데이터 조회해오는 메소드

        //then
        Posts posts = postsList.get(0);
        assertThat(posts.getTitle()).isEqualTo(title);
        assertThat(posts.getContent()).isEqualTo(content);
    }
}
