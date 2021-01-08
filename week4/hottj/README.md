# :cherry_blossom: Spring boot를 이용한 RESTful Web Services 개발

---

## :one: Web Service & Web Application

### :smile: Web Service와 Web Application의 개요

- Spring boot + RESTful API

### :smile: Web Service 개발 방법 SOAP과 REST의 이해

- WebService
  - 네트워크 상에서 서로 다른 종류의 컴퓨터들 간에 상호작용하기 위한 소프트웨어 시스템
    - XML / JSON
  - SOAP과 REST 사용
    - SOAP (Simple Object Access Protocol)
      - XML만, 복잡하고 느리고 무겁다
  ![12](https://user-images.githubusercontent.com/47052106/103463217-307a6100-4d6e-11eb-8bec-2698db7d74b4.JPG)
    - REST (Representational State Transfer)
      - Resource의 Representation에 의한 상태 전달
      - HTTP Method를 통해 Resource를 처리하기 위한 아키텍쳐
      - RESTful
        - REST API를 제공하는 웹 서비스
        - Resource
          - URI (Uniform Resource Identifier), 인터넷 자원을 나타내는 유일한 주소
          - XML, HTML, JSON              
  ![33](https://user-images.githubusercontent.com/47052106/103463219-31ab8e00-4d6e-11eb-9122-0d4f982e0546.JPG)

- API
  - API는 어떤 서버의 특정한 부분에 접속해서 그 안에 있는 데이터와 서비스를 이용할 수 있게 해주는 소프트웨어 도구

![11](https://user-images.githubusercontent.com/47052106/103463069-05dbd880-4d6d-11eb-9bd7-80a08f264c7f.JPG)

## :two: Spring Boot로 개발하는 RESTful Service

### :smile: Spring Boot 개요

- Spring Boot
  - 스프링부트는 단독실행되는, 실행하기만 하면 되는 상용화 가능한 수준의, 스프링 기반 애플리케이션을 쉽게 만들어낼 수 있다.
  - 최소한의 설정으로 스프링 플랫폼과 서드파티 라이브러리들을 사용할 수 있도록 하고 있다.
- Spring Boot Aplication
- Auto Configuration
  - 애플리케이션에 필요한 각종 설정 작업을 작동화, 개발자가 등록한 환경을 불러와 읽기도 함.
- Component Scan

### :smile: REST API 설계

![캡처](https://user-images.githubusercontent.com/47052106/103477247-4afd1a80-4e00-11eb-8eb1-da2239fb9d2b.JPG)

![캡처1](https://user-images.githubusercontent.com/47052106/103477248-4d5f7480-4e00-11eb-9b3f-40ffdf439c35.JPG)

### :smile: Spring Boot Project 생성

- 인텔리제이 커뮤니티 버전은 프로젝트를 생성할때, Spring Initializer가 없다. start.spring.io 에서 생성한뒤 Open 해야한다.

### :smile: Spring Boot Project 구조 확인과 실행 방법

- pom.xml
  - 전체 프로젝트에 필요한 maven 설정을 지정
- application.properties / application.yml
  - 필요한 Spring Boot의 설정을 지정
  - yml 을 더 많이 사용하는 추세
- 포트 변경을 하려면 application.yml 파일을 수정한다.
  ```yml
  server:
    port: 8088
  ```
  
### :smile: HelloWorld Controller 추가
  
```java
@RestController
public class HelloWorldController {
    // GET
    // /hello-world (endpoint)
    // @RequestMapping(method=RequestMethod.GET, path="/hello-world") : 과거의 방식
    @GetMapping(path = "/hello-world")
    public String helloWorld(){
        return "Hello World";
    }
}
```

- Postman으로 실행

![캡처](https://user-images.githubusercontent.com/47052106/103481431-5bbc8900-4e1e-11eb-817b-28f59c2555fe.JPG)

- **@ResController = @Controller + @ResponseBody**
  - *@Controller*
    - ViewResolver가 동작
  - *@ResponseBody*
    - ViewResolver가 동작하지 않고 Converter가 동작
      - JSONConverter
      - StringConverter

### :smile: HelloWorld Bean 추가

- HelloWorldController
```java
package com.example.demo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorldController {
    // GET
    // /hello-world (endpoint)
    // @RequestMapping(method=RequestMethod.GET, path="/hello-world") : 과거의 방식
    @GetMapping(path = "/hello-world")
    public String helloWorld(){
        return "Hello World";
    }

    // alt + enter
    @GetMapping(path = "/hello-world-bean")
    public HelloWorldBean helloWorldBean(){
        return new HelloWorldBean("Hello World"); //자바 빈형태로 반환 -> 텍스트나 object 형태가 아닌 JSON형태로 반환
    }
}
```

- HelloWorldBean
```java
package com.example.demo;
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
```

- 객체를 반환하면, JSON형태로 변환되어 반환된다.

![캡처22](https://user-images.githubusercontent.com/47052106/103483389-4e0d0080-4e2a-11eb-8040-1608004b8f99.JPG)

### :smile: DispatcherServlet과 프로젝트 동작의 이해

- application.properties -> 설정이름 = 값
- application.yml -> 설정이름 : 값
  - xml 보다 보기 편하여 더 많이 사용

![캡처332](https://user-images.githubusercontent.com/47052106/103483872-92e66680-4e2d-11eb-9abb-1816ed5e759c.JPG)

![캡처4](https://user-images.githubusercontent.com/47052106/103483873-94179380-4e2d-11eb-928c-c86b1a0afda0.JPG)

### :smile: Path Variable 사용

```java
// 가변 변수 name 과 매개변수 name이 다른 경우에는 @PathVariable 매개변수로 value=name(가변변수)로 매핑해줘야한다.
//    @GetMapping(path = "/hello-world-bean/path-variable/{name}")
//    public HelloWorldBean helloWorldBean(@PathVariable(value="name") String name1){ // HelloWorldBean()과매개변수가 다르므로 오버로딩
//        return new HelloWorldBean(String.format("Hello World, %s", name1));
//    }


    @GetMapping(path = "/hello-world-bean/path-variable/{name}")
    public HelloWorldBean helloWorldBean(@PathVariable String name){ // HelloWorldBean()과매개변수가 다르므로 오버로딩
        return new HelloWorldBean(String.format("Hello World, %s", name));
    }
}
```

`http://localhost:8088/hello-world-bean/path-variable/hakjae` 로 Send 하면,
```json
{
    "message": "Hello World, hakjae"
}
```

## :three: User Service API 구현

### :smile: User 도메인 클래스 생성

- 도메인
  - 인간 활동영역, 자율적 컴퓨터 활동, 특정한 전문 분야에서 사용되는 업무 지식
    - 사용자 ID, 사용자 이름, 사용자 가입날짜 등과 같은 정보

- 사용자 정보 작성
```java
package com.example.demo.user;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class User {
    private Integer id; // 도메인 부분
    private String name;
    private Date joinDate;
}
```

- 사용자 서비스 작성
```java
package com.example.demo.user;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UserDaoService {
    private static List<User> users = new ArrayList<>(); // DB 역할하는 리스트

    private static int usersCount = 3;
    static{ //static 으로 만들었기때문에 static 블록에서 사용 가능하다
        users.add(new User(1,"Hottj1",new Date()));
        users.add(new User(2,"Hottj2",new Date()));
        users.add(new User(3,"Hottj3",new Date()));
    }

    public List<User> findAll(){
        return users;
    }

    public User save(User user){
        if (user.getId() == null){
            user.setId(++usersCount);
        }

        users.add(user);
        return user;
    }

    public User findOne(int id) {
        for (User user : users) {
            if (user.getId() == id) {//lombok 을 사용해서 바로 getId() 사용 가능하다
                return user;
            }
        }
        return null;
    }
}
```

### :smile: 사용자 목록 조회를 위한 API 구현 - GET HTTP  Method

- 사용자 컨트롤러 작성
```java
package com.example.demo.user;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service // 서비스 빈으로 등록 @Component 라고 해도 되지만 좀 더 명시적인 서비스 사용
public class UserDaoService {
    private static List<User> users = new ArrayList<>(); // DB 역할하는 리스트

    private static int usersCount = 3;
    static{ //static 으로 만들었기때문에 static 블록에서 사용 가능하다
        users.add(new User(1,"Hottj1",new Date()));
        users.add(new User(2,"Hottj2",new Date()));
        users.add(new User(3,"Hottj3",new Date()));
    }

    public List<User> findAll(){
        return users;
    }

    public User save(User user){
        if (user.getId() == null){
            user.setId(++usersCount);
        }

        users.add(user);
        return user;
    }

    public User findOne(int id) {
        for (User user : users) {
            if (user.getId() == id) {//lombok 을 사용해서 바로 getId() 사용 가능하다
                return user;
            }
        }
        return null;
    }
}
```

### :smile: 사용자 등록을 위한 API 구현 - POST HTTP Method

- @RequestBody
  -  @RequestBody는 클라이언트가 전송하는 Http 요청의 Body내용을 Java Object로 변환시켜주는 역할을 한다. 그렇기 때문에 Body가 존재하지 않는 Get 방식의 메소드에 @RequestBody를 활용하는 것은 적합하지 않으므로, 에러가 발생하게 된다. 즉, @RequestBody는 반드시 Post 요청과 함께 사용되어야 한다. @RequestBody는 Json이나 XML과 같은 형태의 데이터를 Jackson 등의 MessageConverter를 활용하여 Java Object로 변환한다. 이러한 성질은 Parameter로 받은 데이터들을 자바 객체로 1대1로 매킹시켜주는 @ModelAttribute와 차이가 있다. 즉, @RequestBody는 POST방식으로 Json의 형태로 넘겨온 데이터를 객체로 바인딩하기 위해 사용할 수 있다.

- @ModelAttribute
  - @ModelAttribute는 클라이언트가 전송하는 여러 파라미터들을 1대1로 객체에 바인딩하여 다시 View로 넘겨서 출력하기 위해 사용되는 오브젝트이다. @ModelAttribute에는 매핑시키는 파라미터의 타입이 객체의 타입과 일치하는지를 포함한 다양한 검증(Validiation) 작업이 추가적으로 진행된다. 예를 들어 게시물의 번호를 저장하는 int형 index 변수에 "1번" 이라는 String형을 넣으려고 한다면, BindException이 발생하게 된다. @RequestBody의 경우에는 Json이나 XML을 Jackson과 같은 MessageConverter를 사용하여 변환시키는 반면에, @ModelAttribute는 여러 개의 파라미터를 바로 자바빈 객체로 매핑시킨다는 차이가 있다. 그렇기 때문에 @ModelAttribute는 JSP에서 Form 태그를 통해 전달받은 파라미터들을 객체로 바인딩 시키는 경우에 사용할 수 있다.
@ModelAttribute와 @RequestBody를 보다 극단적으로 설명하자면, @ModelAttribute는 바인딩시키는 어떤 데이터를 set해주는 Setter함수가 없다면 매핑이 되지 않는다. 하지만 @RequestBody는 요청받은 데이터를 변환시키는 것이기 때문에, Setter함수가 없어도 값이 매핑이 된다.
@ModelAttribute 어노테이션을 활용해서 특정 Parameter만을 받아울 수 있다. 만약 우리가 { writer: '망나니개발자', contents : '안녕하세요, 망나니 개발자입니다.' }의 형태로 데이터를 전송했다고 하면, 컨트롤러에서는 @ModelAttribute('writer') String writer의 형태를 활용하여 writer 변수에 '망나니개발자' 만을 바인딩시켜 받아올 수 있다.

- @RequestParam
  - @RequestParamd은 요청 파라미터를 메소드에서 1:1로 받기 위해서 사용한다. @RequestParam를 사용하면 기본적으로 반드시 해당 파라미터가 전송되어야 한다. 즉, 반드시 필요한 파라미터인가?를 나타내는 required의 값이 default로 true로 되어있어서, 해당 파라미터가 전송되지 않으면 400 Error를 유발하게 된다. 그렇기 때문에 반드시 필요한 변수가 아니라면 required의 값을 false로 설정해둘 수 있으며 해당 Parameter를 사용하지 않고 요청을 보낼 경우에 default로 받을 값을 설정할 수도 있다. 즉, defaultValue로 기본값을 설정할 수 있으며, 필수 요구사항인지를 required로 설정할 수 있다.
  
### :smile: HTTP Status Code 제어

- Rest API를 구현 하다 보면 사용자로 부터 요청왔을때  특정값을 포함한 uri를 전달해야 하는 상황이 발생할 수 있다. 이때 사용하는 것이 ServletUriComponentsBuilder이다.  ServletUriComponentsBuilder를 통해 적절한 URI를 만들고 요청한 사용자에게 특정값을 포함한 URI를 전달 할 수 있다. 

![캡처](https://user-images.githubusercontent.com/47052106/103575634-5db54380-4f15-11eb-9031-2dee1082b9ea.JPG)

- Response Headers의 location 부분을 보면 controller에서 만든 uri가 전달 된것을 확인 할 수 있습니다. 
- PostMan으로 요청 할 결과를 보시면 Status코드가 201 Created인 것을 확인 할 수 있습니다. 이를 통하여 적절한 Rest API를 만들 수 있습니다. 

### :smile: HTTP Status Code 제어를 위한 Exception Handling

![캡처11](https://user-images.githubusercontent.com/47052106/103579229-42e5cd80-4f1b-11eb-8fe5-a5c5a0b5d525.JPG)
- 보안상의 문제가 생길 수 있다.

![캡처33](https://user-images.githubusercontent.com/47052106/103579235-4416fa80-4f1b-11eb-87ff-e3f12afeee48.JPG)
- 해당 데이터가 없을 경우, 404 NOT FOUND로 처리한다. 

### :smile: Spring AOP를 이용한 Exception Handling

- 핸들러 클래스를 만들어서 이를 오류 핸들링 메시지로 커스터마이즈하여 RESTful 하게 한다.

![캡처44](https://user-images.githubusercontent.com/47052106/103583601-56953200-4f23-11eb-913f-e358100a07ad.JPG)
- 보안상의 문제 해결 (trace가 사라짐)

### :smile: 사용자 삭제를 위한 API 구현 - DELETE HTTP Method

- 삭제를 위해서 @DeleteMapping 을 사용한다.
- REST API 는 GET , POST , DELETE , PUT 총 네가지를 사용할 수 있다.
  - GET 은 조회, select
  - POST 은 추가, insert
  - DELETE 는 삭제, remove
  - PUT 은 수정, update

