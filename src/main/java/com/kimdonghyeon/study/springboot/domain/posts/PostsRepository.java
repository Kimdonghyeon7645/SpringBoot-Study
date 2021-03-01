package com.kimdonghyeon.study.springboot.domain.posts;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/*
 Repository : ibatis, MyBatis 등에서 Dao(다오)로 불리는 DB Layer 접근자
 JPA에선 Dao를 repository라 부르며, 인터페이스로 생성
 -> 인터페이스 생성 후, JpaRepository<엔티티 클래스명, PK타입> 을 상속하면 기본 CRUD 메소드가 자동 생성

 - @Repository를 추가할 필요 없음
 - 엔티티(Entity) 클래스와 기본 엔티티 repository는 함께 있어야 함(엔티티 클래스는 기본 repository 없이는 제대로된 역할 불가)
 - 도메인 별로 프로젝트 분리시, 엔티티 클래스와 기본 repository는 함께 움직여야 하므로, 도메인 패키지에서 함께 관리
 */
public interface PostsRepository extends JpaRepository<Posts, Long> {

    @Query("SELECT p FROM Posts as p ORDER BY p.id DESC")
    List<Posts> findAllDesc();
    /*
     사실 SpringDataJpa에서 제공하는 기본 메소드만으로 위의 메소드를 대체할 수 있다.
     하지만 위같이 SpringDataJpa에서 메소드를 제공하지 않더라도 쿼리를 작성해서(@Query) 만들 수 있다.

     @Query를 사용한다면, 쿼리문을 명시하여 가독성이 좋아지니,
     제공하는 메소드와 직접 쿼리문을 작성한 메소드, 2가지 중 원하는 걸 선택해서 쓰면 된다.
    */
}

/*
 규모가 큰 프로젝트에선 데이터 조회에 FK조인 등 복잡한 조건이 들어가서 위같은 Entity 클래스만으로 처리하기 어렵다.
 -> 조회용 프레임워크(대표적으로 Querydsl, Jooq, MyBatis)을 사용한다.

 - 조회 = 조회용 프레임워크
 - 등록/수정/삭제 = SpringDataJpa

 책의 저자는 조회용 프레임워크의 3가지 중 Querydsl을 추천한다. 이유 3가지로는
  1. 타입 안정성 보장 : 문자열로 쿼리를 생성(x) -> 메소드 기반으로 쿼리 생성(o), 오타나 존재하지 않는 컬럼명은 IDE에서 자동 검출 (Jooq에서도 지원하지만, MyBatis는 미지원)
  2. 국내 많은 회사에서 사용중 : 쿠팡, 배민 등 JPA 쓰는 회사에서 Querydsl도 적극 사용
  3. 레퍼런스 많음 : 국내 활용도가 많다보니 국내 자료도 많음
*/
