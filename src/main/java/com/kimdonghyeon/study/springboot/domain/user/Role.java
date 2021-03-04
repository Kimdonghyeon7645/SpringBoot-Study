package com.kimdonghyeon.study.springboot.domain.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Role {

    GUEST("ROLE_GUEST", "손님"),
    USER("ROLE_USER", "일반 사용자");
    /*
     스프링 시큐리티에서는 권한 코드에 항상 ROLE_이 앞에 있어야 한다.
     */

    private final String key;
    private final String title;
}
