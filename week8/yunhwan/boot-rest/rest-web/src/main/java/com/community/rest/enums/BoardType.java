package com.community.rest.enums;

public enum BoardType {
    notice("공지사항"),
    free("자유게시판");

    private String value;

    // 생성자의 인자로 넘어온 값을 통해 value값을 설정해줄 수 있다.
    BoardType(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
