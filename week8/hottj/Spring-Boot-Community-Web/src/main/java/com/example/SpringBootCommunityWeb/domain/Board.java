package com.example.SpringBootCommunityWeb.domain;

import com.example.SpringBootCommunityWeb.domain.enums.BoardType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
@Table
public class Board implements Serializable {

    // GeneratedValue로 기본 키가 자동으로 할당되도록 설정
    // GenerationType.IDENTITY로 키 생성을 DB에 위임
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @Column
    private String title;

    @Column
    private String subTitle;

    @Column
    private String content;

    // @Enumerated(EnumType.STRING)으로 enum과 데이터베이스 데이터 변환을 지원
    @Column
    @Enumerated(EnumType.STRING)
    private BoardType boardType;

    @Column
    private LocalDateTime createdDate;

    @Column
    private LocalDateTime updatedDate;

    // User 도메인과 1:1 관계를 설정
    // LAZY 옵션으로 User 객체를 조회하는 시점에 조회하게 하는 설정
    // HATEOAS를 제공하는데, User는 JSON으로 변환하지 않는 객체이므로 @JsonIgnore 어노테이션 추가
    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    private User user;

    public void setCreatedDateNow() {
        this.createdDate = LocalDateTime.now();
    }

    public void setUpdatedDate() {
        this.updatedDate = LocalDateTime.now();
    }

    public void update(Board board){
        this.title = board.getTitle();
        this.subTitle = board.getSubTitle();
        this.content = board.getContent();
        this.boardType = board.getBoardType();
        this.updatedDate = LocalDateTime.now();
    }

    @Builder
    public Board(String title, String subTitle, String content, BoardType boardType, LocalDateTime createdDate, LocalDateTime updatedDate, User user) {
        this.title = title;
        this.subTitle = subTitle;
        this.content = content;
        this.boardType = boardType;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
        this.user = user;
    }
}
