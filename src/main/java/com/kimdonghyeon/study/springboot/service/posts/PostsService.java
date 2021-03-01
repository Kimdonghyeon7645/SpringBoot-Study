package com.kimdonghyeon.study.springboot.service.posts;

import com.kimdonghyeon.study.springboot.domain.posts.Posts;
import com.kimdonghyeon.study.springboot.domain.posts.PostsRepository;
import com.kimdonghyeon.study.springboot.web.dto.PostsListResponseDto;
import com.kimdonghyeon.study.springboot.web.dto.PostsResponseDto;
import com.kimdonghyeon.study.springboot.web.dto.PostsSaveRequestDto;
import com.kimdonghyeon.study.springboot.web.dto.PostsUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PostsService {
    private final PostsRepository postsRepository;

    @Transactional
    public Long save(PostsSaveRequestDto requestDto) {
        return postsRepository.save(requestDto.toEntity()).getId();
    }

    @Transactional
    public Long update(Long id, PostsUpdateRequestDto requestDto) {
        Posts posts = postsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("id가" + id + "인 게시글이 없습니다."));
        posts.update(requestDto.getTitle(), requestDto.getContent());

        return id;
    }

    public PostsResponseDto findById(Long id) {
        Posts entity = postsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("id가" + id + "인 게시글이 없습니다."));

        return new PostsResponseDto(entity);
    }

    @Transactional(readOnly = true)
    // readOnly = true : 트렌젝션 범위는 유지하되, 조회 기능만 남겨둠 -> 조회 속도 개선 (등록, 수정, 삭제 기능이 전혀없는 서비스 메소드에서 사용하는 것을 추천)
    public List<PostsListResponseDto> findAllDesc() {
        return postsRepository.findAllDesc().stream()
                .map(PostsListResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public void delete(Long id) {
        Posts posts = postsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id=" + id));

        postsRepository.delete(posts);
        /*
         JpaRepository에서 이미 delete 메소드를 지원하고 있어서 이를 활용,
         deleteById 메소드로 id로써 삭제할 수 있으나, 여기선 존재하는지 확인 위해서, 엔티티 조회후 바로 삭제
        */
    }
}
