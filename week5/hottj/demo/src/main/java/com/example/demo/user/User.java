package com.example.demo.user;


import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.util.Date;

@Data
@AllArgsConstructor
//@JsonIgnoreProperties(value={"password","ssn"}) // 무시하고 싶은 필드명을 value에 지정
@NoArgsConstructor // 디폴트 생성자가 있어야 상속가능해진다.
@JsonFilter("UserInfo")// 임의로 이름 지정
public class User {
    private Integer id;

    @Size(min=2, message = "Name은 2글자 이상 입력해 주세요.") // 이름 두글자 이상 입력, 디폴트 메시지 입력. 오류시 details에 보임
    private String name;
    @Past // 과거 데이터만
    private Date joinDate;

//    @JsonIgnore // 보이지 않게끔 함
    private String password;
    //   @JsonIgnore
    private String ssn; // 주민번호
}
