package com.kimdonghyeon.study.springboot.domain.posts;

import com.kimdonghyeon.study.springboot.domain.posts.Posts;
import com.kimdonghyeon.study.springboot.domain.posts.PostsRepository;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest     // 별 설정이 없으면 H2 데이터베이스를 자동으로 실행
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

        postsRepository.save(Posts.builder()
                .title(title)
                .content(content)
                .author("dongdongYee@gmail.com")
                .build());
        // postsRepository.save : 테이블 posts에서, 인자값에 id값이 있으면 update, 없으면 insert 쿼리 실행
        // save() 시 이전에 조회한 후, 기존에 있는 데이터는 update, 없던 새로운 데이터는 insert 

        //when
        List<Posts> postsList = postsRepository.findAll();      // findAll() : posts 테이블의 모든 데이터 조회해오는 메소드

        //then
        Posts posts = postsList.get(0);
        assertThat(posts.getTitle()).isEqualTo(title);
        assertThat(posts.getContent()).isEqualTo(content);
    }

    @Test
    public void registerBaseTimeEntity() {
        //given
        LocalDateTime now = LocalDateTime.of(2021, 2, 24, 21, 31, 0);
        postsRepository.save(Posts.builder()
                .title("제목")
                .content("내용")
                .author("글쓴이")
                .build());
        //when
        List<Posts> postsList = postsRepository.findAll();

        //then
        Posts posts = postsList.get(0);

        System.out.println("==== createDate = " + posts.getCreateDate() + ", modifiedDate = " + posts.getModifiedDate());

        assertThat(posts.getCreateDate()).isAfter(now);
        assertThat(posts.getModifiedDate()).isAfter(now);
    }
}
