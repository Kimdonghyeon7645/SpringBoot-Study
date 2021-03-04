package com.kimdonghyeon.study.springboot.config.auth.dto;

import com.kimdonghyeon.study.springboot.domain.user.User;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class SessionUser implements Serializable {
    private String name;
    private String email;
    private String picture;

    public SessionUser(User user) {
        this.name = user.getName();
        this.email = user.getEmail();
        this.picture = user.getPicture();
    }
}
/*
 User 클래스를 그대로 사용 못하는 이유 = 직렬화 구현 안해서 에러남
 그렇다고 User 클래스에 직렬화 코드를 넣는 것은 문제 = User 클래스가 엔티티여서
 (엔티티 클래스를 직렬화하면, 다른 엔티티와의 관계가 있으면 그것도 직렬화 -> 성능 이슈, 부수 효과 발생 가능성 높음)

 그래서 직렬화 기능을 가진 세션 Dto를 추가로 만드는 것이 운영, 유지보수에 도움됨
 */
