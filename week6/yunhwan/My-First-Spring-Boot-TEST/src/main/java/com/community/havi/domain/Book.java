package com.community.havi.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Entity // 데이터베이스 테이블과 1:1로 매칭되는 객체단위
@Table  // 맵핑할 테이블 속성 지정, 테이블 이름 지정 등
public class Book {

    @Id
    @GeneratedValue
    private Integer idx;

    @Column
    private String title;
    @Column
    private LocalDateTime publishedAt;

    @Builder // 빌더 패턴 적용
    public Book(String title, LocalDateTime publishedAt) {
        this.title = title;
        this.publishedAt = publishedAt;
    }
}
