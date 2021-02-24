package com.kimdonghyeon.study.springboot.domain;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter
@MappedSuperclass
// JPA Entity 클래스들이 BaseTimeEntity 클래스를 상속할 경우 여기의 클래스 필드들(createdDate, modifiedDate)도 컬럼으로 인식하도록 해줌
@EntityListeners(AuditingEntityListener.class)      //BaseTimeEntity 클래스에 Auditing 기능을 포함시킴
// Auditing : Audit -> 감시, 감사하다란 뜻으로, Spring Data JPA 에서 시간에 대해서 자동으로 값을 넣어주는 기능 https://webcoding-start.tistory.com/53
public abstract class BaseTimeEntity {

    @CreatedDate        // Entity가 생성되어 저장될 때, 시간도 자동 저장됨
    private LocalDateTime createDate;

    @LastModifiedDate       // 조회한 Entity의 값을 변경할 때, 시간도 자동 저장됨
    private LocalDateTime modifiedDate;
}
