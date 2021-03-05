package com.kimdonghyeon.study.springboot.config.auth;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)
/* @Target(ElementType.PARAMETER)
 이 어노테이션이 생성될 수 있는 위치 지정
 PARAMETER : 메소드의 파라미터로 선언된 객체에서만 사용 가능
 */
@Retention(RetentionPolicy.RUNTIME)
/* @Retention(RetentionPolicy.RUNTIME)
 어노테이션 유지 정책을 설정
 RUNTIME은
 */
public @interface LoginUser {
    /* @interface
     이 파일을 어노테이션 클래스로 지정
     */

}
