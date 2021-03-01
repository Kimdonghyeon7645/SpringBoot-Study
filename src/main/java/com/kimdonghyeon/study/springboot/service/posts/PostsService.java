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
    public List<PostsListResponseDto> findAllDesc() {
        return postsRepository.findAllDesc().stream()
                .map(PostsListResponseDto::new)
                .collect(Collectors.toList());
    }
}
