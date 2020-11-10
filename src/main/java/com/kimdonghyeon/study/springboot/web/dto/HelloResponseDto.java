/*
롬복, 자바 개발자들의 필수 라이브러리
롬복은 개발에 자주 사용되는 Getter, Setter, 기본생성자, toString 등을 어노테이션으로 자동 생성
인텔리제이에선 플러그인으로 쉽게 다운 + build.grade에 롬복 라이브러리(의존성)을 추가후 새로고침해 내려받기
마지막으로 Settings > Build, Execution, Deployment > Compiler > Annotation Processor 에서 Enable annotation processing 체크
(플러그인은 처음 한번만, build.grade에 라이브러리 추가 + 설정에 체크박스 체크는 프로젝트마다 설정 해야함)
 */
package com.kimdonghyeon.study.springboot.web.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter     // 선언된 모든 필드의 get 메소드를 생성 (롬복)
@RequiredArgsConstructor    // 선언된 모든 final 필드가 포함된 생성자를 생성 (final이 없는 필드는 생성자에 포함되지 않음)
public class HelloResponseDto {

    private final String name;
    private final int amount;

}
