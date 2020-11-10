package com.kimdonghyeon.study.springboot.web.dto;

import org.junit.Test;
import static org.assertj.core.api.Assertions.assertThat;
/*
여기선 JUnit의 기본 assertThat이 아닌, assertj의 assertThat을 사용
-> JUnit과 비교했을때 assertj의 장점
1. 추가적으로 (CoreMatchers같은) 라이브러리가 필요하지 않음
2. 자동완성이 확실하게 지원
 */

public class HelloResponseDtoTest {

    @Test
    public void LombokTest() {
        // given (주어지는 거)
        String name = "test";
        int amount = 100;

        // when (경우)
        HelloResponseDto dto = new HelloResponseDto(name, amount);

        // then (경우에 따른 결과)
        assertThat(dto.getName()).isEqualTo(name);
        assertThat(dto.getAmount()).isEqualTo(amount);
        /*
        assertThat : assertj(테스트 검증 라이브러리)의 검증 메소드, 검증하고 싶은 대상을 메소드 인자로 받음
        메소드 체이닝 지원 -> isEqualTo 와 같이 메소드 연결 가능
        isEqualTo : assertj 의 동등 비교 메소드 (assertThat의 값과 isEqualTo의 값이 같을때만 성공)
         */
    }
}
