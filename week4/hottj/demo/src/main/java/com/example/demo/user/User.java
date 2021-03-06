package com.example.demo.user;


import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.util.Date;

@Data
@AllArgsConstructor
public class User {
    private Integer id;

    @Size(min=2, message = "Name은 2글자 이상 입력해 주세요.") // 이름 두글자 이상 입력, 디폴트 메시지 입력. 오류시 details에 보임
    private String name;
    @Past // 과거 데이터만
    private Date joinDate;
}
