package com.kimdonghyeon.study.springboot.domain.posts;

import org.springframework.data.jpa.repository.JpaRepository;

/*
Repository : ibatis, MyBatis 등에서 Dao(다오)로 불리는 DB Layer 접근자
JPA에선 Dao를 repository라 부르며, 인터페이스로 생성
-> 인터페이스 생성 후, JpaRepository<엔티티 클래스명, PK타입> 을 상속하면 기본 CRUD 메소드가 자동 생성

* @Repository를 추가할 필요 없음
* 엔티티(Entity) 클래스와 기본 엔티티 repository는 함께 있어야 함(엔티티 클래스는 기본 repository 없이는 제대로된 역할 불가)
* 도메인 별로 프로젝트 분리시, 엔티티 클래스와 기본 repository는 함께 움직여야 하므로, 도메인 패키지에서 함께 관리
 */
public interface PostsRepository extends JpaRepository<Posts, Long> {

}
