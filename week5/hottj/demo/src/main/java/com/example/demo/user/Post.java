package com.example.demo.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Post {
    @Id
    @GeneratedValue
    private Integer id;

    private String description;

    // User : Post -> 1: (0~N). User가 메인 데이터. Main : Sub -> Parent : Child
    @ManyToOne(fetch = FetchType.LAZY) // post가 여러개 올 수 있고, 하나의 값과 매칭. (User 하나와).
    // 지연로딩방식. 포스트 데이터가 로딩되는 시점에 필요한 데이터를 가져온다는 뜻
    @JsonIgnore // JSON으로 출력될 때 사용자는 외부에 공개되지 않는다.
    private User user;
}
