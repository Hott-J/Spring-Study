package com.example.demo.helloworld;
// lombok : Bean 클래스를 만들 때 메소드를 자동으로 만들어줌.

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data //File -> Settings -> Annotation processors -> Enable Annotation processing 체크해야 lombok 이 지원하는 모든 기능 사용가능
@AllArgsConstructor //생성자 자동으로 생성
@NoArgsConstructor // 디폴트 생성자 생성
public class HelloWorldBean {
    private String message;

//    public String getMessage(){
//        return this.message;
//    }
//    public void setMessage(String msg){
//        this.message=msg;
//    } lombok 이 있으므로, setter,getter 함수 작성 안해도 된다.

//    public HelloWorldBean(String message){
//        this.message=message;
//    } //AllArgsConstrucuor 가 있으므로 작성안해도 된다. 인텔리제이 plugin lombok 을 설치해서 빨간줄 표시하게끔 setting 설정.
}
