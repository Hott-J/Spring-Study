package com.example.demo.user;


import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
//@JsonIgnoreProperties(value={"password","ssn"}) // 무시하고 싶은 필드명을 value에 지정
@NoArgsConstructor // 디폴트 생성자가 있어야 상속가능해진다.
//@JsonFilter("UserInfo")// 임의로 이름 지정
@ApiModel(description = "사용자 상세 정보를 위한 도메인 객체") // swagger documentation
@Entity // 테이블 생성
public class User {
    @Id // 속성
    @GeneratedValue // 자동 생성
    private Integer id;

    @Size(min=2, message = "Name은 2글자 이상 입력해 주세요.") // 이름 두글자 이상 입력, 디폴트 메시지 입력. 오류시 details에 보임
    @ApiModelProperty(notes="사용자 이름을 입력해 주세요.")  // swagger documentation
    private String name;
    @Past // 과거 데이터만
    @ApiModelProperty(notes="사용자의 등록일을 입력해 주세요.")  // swagger documentation
    private Date joinDate;

//    @JsonIgnore // 보이지 않게끔 함
    @ApiModelProperty(notes="사용자의 패스워드를 입력해 주세요.")  // swagger documentation
    private String password;
    //   @JsonIgnore
    @ApiModelProperty(notes="사용자의 주민번호를 입력해 주세요.") // swagger documentation
    private String ssn; // 주민번호

    // 게시판
    @OneToMany(mappedBy = "user") // 하나가 여러개와 매칭. 하나의 유저와 여러개의 게시판과 매칭. 관계형데이터베이스에서의 관계
    private List<Post> posts;
    
    // posts 데이터가 빠져있는 생성자가 필요하다. UserDaoService 에서 users.add(~) 메소드 실행하기 위해서 
    public User(int id, String name, Date joinDate, String password, String ssn) {
        this.id = id;
        this.name= name;
        this.joinDate = joinDate;
        this.password = password;
        this.ssn = ssn;
    }
}
