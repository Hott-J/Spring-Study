package com.example.demo.user;


import com.fasterxml.jackson.annotation.JsonFilter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.util.Date;

@Data
@AllArgsConstructor
//@JsonIgnoreProperties(value={"password","ssn"}) // 무시하고 싶은 필드명을 value에 지정
@NoArgsConstructor
@JsonFilter("UserInfoV2")// 임의로 이름 지정
public class UserV2 extends User{
    private String grade;
}
