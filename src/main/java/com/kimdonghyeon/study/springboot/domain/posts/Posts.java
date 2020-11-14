package com.kimdonghyeon.study.springboot.domain.posts;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter     // <롬복> 클래스의 모든 필드 Getter 메소드 자동생성
@NoArgsConstructor      // <롬복> 기본 생성자 자동 추가 (public Posts(){} 와 같은 효과)
@Entity     // 테이블과 링크(매칭)될 클래스라는 것을 의미 (기본값 : 클래스명과 동일한 (네이밍은 카멜케이스가 아닌 스네이크케이스로) 테이블명을 매칭)
public class Posts {    // 여기서 Posts 클래스 -> 실제 DB 테이블과 매칭될 클래스 = "Entity(엔티티) 클래스"

    @Id     // 해당 테이블의 PK 필드를 의미
    @GeneratedValue(strategy = GenerationType.IDENTITY)     // PK 생성규칙 의미 (GenerationType.IDENTITY == auto_increment(자동 증감번호))
    private Long id;

    @Column(length = 500, nullable = false)
    /*
    테이블의 컬럼 의미 (어노테이션 붙이지 않아도 해당 클래스의 모든 필드 = 컬럼)
    쓰는 이유는 지금같이 타입(columnDefinition), 컬럼의 길이(length)등을 기본값과 다르게 변경시 사용
     */
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    private String author;

    @Builder        // <롬복> 해당 클래스의 빌더 패턴 클래스 생성 (지금처럼 생성자 상단에 선언시 생성자에 포함된 필드만 빌더에 포함)
    public Posts(String title, String content, String author) {
        this.title = title;
        this.content = content;
        this.author = author;
    }
}

/*
1. 여러 어노테이션을 작성하는 순서는 주요 어노테이션을 클래스에 가깝게 두면, 부가기능(롬복등의) 어노테이션을 쉽게 삭제 가능
2. 서비스 초기 구축 단계 -> 테이블(여기선 엔티티)설계가 자주 바뀌는데, 롬복 어노테이션으로 코드 변경량을 최소화 가능 (적극적으로 활용하자)
3. 엔티티 클래스에선 절대 setter 메소드를 만들지 않는다 -> 클래스의 인스턴스 값이 언제 어디서 변해야 되는지 구분 불가, 복잡해짐
4. setter 메소드가 없는대신, 기본으론 생성자(여기선 생성자대신 빌더 클래스)통해 값을 저장후 DB에 삽입, 값 변경 필요시 해당 이벤트에 맞는 public 메소드 호출
5. 빌더 클래스를 활용하면 생성자보다 어느 필드에 어떤 값을 채워야 할지 명확하게 구분됨 (적극적으로 활용하자)
 */