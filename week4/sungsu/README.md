# :cherries: RESTful service

---
## :tulip: 개요
### :one: Web Service & Web Application
- 네트어크 상에서 서로 다른 종류의 컴퓨터들 간에 상호작용하기 위한 소프트웨어 시스템

### :two: RESTful
- Soap방식에 대체된 방법
- Resource의 Representation에 의한 상태 전달
- HTTP Method를 통해 Resource를 처리하기 위한 아키텍처
    - REST는 기본적으로 웹의 기존 기술과 HTTP 프로토콜을 그대로 활용하기 때문에 웹의 장점을 최대한 활용할 수 있는 아키텍처 스타일이다.
    - REST는 네트워크 상에서 Client와 Server 사이의 통신 방식 중 하나이다.

#### :smile: 자원이란?
- 해당 소프트웨어가 관리하는 모든것
- ex) 문서, 그림, 데이터, 해당 소프트웨어 자체 등
#### :smile: 자원의 표현이란?
- 자원의 표현 : 그 자원을 표현하기 위한 이름
- DB의 학생정보가 자원일 때, 'student'를 자원의 표현으로 정한다.
#### :smile: 상태 정보 전달
- 데이터가 요청되어지는 시점에서 자원의 상태를 전달한다.
- Json이나 XML을 통해 데이터를 주고 받는 것이 일반적이다.

#### :smile: SOAP vs REST
- restrictions vs architectural approach
- Data Exchange Format
- Service Definition
- Transport
- Ease of implementation

## :tulip: spring boot
### :one: spring boot
- packaging: 끝나고 압축할 때 어떤파일로 할지, JAR or WAR 설정
### :two: restfulAPi 맛보기
-user -> posts
![restful](https://user-images.githubusercontent.com/51367515/104756760-8e13a200-579f-11eb-8e77-deba5c22b3c4.PNG)

### :three: 
- packaging: 끝나고 압축할 때 어떤파일로 할지, JAR or WAR 설정
- 사용 dependency: spring boot devTool, Lombook, Spring Web, Spring Data JPA, H2 Database -> pom.xml에서 확인 가능
- LifeCycle
    - clean: 지금 까지 빌드 혹은 패키징 했던 모든 파일을 지움
    - compile : complie 후 클래스 생성
    - package : jar 또는 war 파일로 packaging
    - install : local 서버에 배포
- 실행시 Embedded tomcat 실행-> 8080 포트번호로 실행
    - 만약 포트번호를 바꾸고 싶다면?->application.yml(요즘 더 선호) or application.property 에서 변경가능 
```
server:
    port: 8088
```
### :four: HelloController 실행
- Controller가 아닌 RestController 사용
- @GetMapping(path = "/hello-world") 형식으로 GET 함수 사용
    - (= @RequestMapping(Method=RequestMethod.GET, path="/hello-world")
- postMan으로 출력 결과 확인 가능
![postman](https://user-images.githubusercontent.com/51367515/104822442-dc02d580-5885-11eb-9f7e-6b564fc16cab.PNG)

### :five: HelloController 실행
```java
@GetMapping(path = "/hello-world-bean")
    public HelloWorldBean helloWorldBean(){
        return new HelloWorldBean("Hello World");

    }
```
- 위와 같은 코드 작성 후 HelloWorldBean이라는 클래스가 존재하지 않음을 알 수 있으므로 생성 해준다
    - lombok dependency를 사용할 때 setter, getter, 생성자 등을 자동으로 생성해준다->@Data,@AllArgsConstructor annotation으로 사용가능
        - :smile: intelliJ에서 lombok설치 필수
```java
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//lombok
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HelloWorldBean {
    private String message;

}
```
- RestController에서 java bean형태로 반환할 경우 json 형식으로 변환에서 반환

### :six: DispatcherServlet과 프로젝트 동작의 이해
- Spring boot 동작원리
    - application.yml or application.properties 를 사용해서 관리가능
        - application.property -> 설정이름=값
        - application.yml -> 설정이름:값
        - ```
        logging:
            level:
                org.springframework: DEBUG
        ```
    - DisplatcherServlet -> '/'
        - 클라이언트의 모든 요청을 한곳으로 받아서 처리(일종의 Gate 역할)
        - 요청에 맞는 handler로 요청을 전달
        - handler의 실행 결과를 Http Response 형태로 만들어서 반환
    - RestController
        - Spring4부터 @RestController 지원
        - @Controller + @ResponseBody
        - View를 갖지 않는 REST DATA(JSON/XML)을 반환
        ![RestController](https://user-images.githubusercontent.com/51367515/104823130-26d31c00-588b-11eb-9a11-6cf9bdc33e49.PNG)
### :seven: Path Variable
- 가변 데이터로 사용할 수 있음

## :tulip: user Service API 구현
### :one: user 도메인 클래스 생성
- user class 생성
```java
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class User {
    private Integer id;
    private String name;
    private Date joinDate;
}
```

- controller 수정
```java
package com.example.restfulwebservice.user;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UserDaoService {
    private static List<User> users= new ArrayList<>();
    private static int usersCount=3;
    static {
        users.add(new User(1, "Kenneth", new Date()));
        users.add(new User(2, "Alice", new Date()));
        users.add(new User(3, "Elena", new Date()));
    }

        public List<User> findAll(){
            return users;
        }
        public User save(User user){
            if(user.getId()==null){
                user.setId(++usersCount);
            }
            users.add(user);
            return user;

        }

        public User findOne(int id){
            for (User user : users){
                if (user.getId()==id){
                    return user;
                }
            }
            return null;
        }
    }
```
### :two: 사용자 목록 조회 구현 
- User controller 구축
    - 의존성 주입 필요(생성자 이용)
    - :smile: 스프링에 등록되서 사용되는 인스턴스를 '빈'이라고 부른다
    - @component를 이용하여 bean 등록
```java
package com.example.restfulwebservice.user;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserController {
    private UserDaoService service;

    public UserController(UserDaoService service) {
        this.service=service;
    }

    @GetMapping("/users")
    public List<User> retrieveAllUsers(){
        return service.findAll();
    }

    @GetMapping("/users/{id}")
    public User retrieveUser(@PathVariable int id){
        return service.findOne(id);
    }
}

```

### :three: 사용자 등록 구현
- post 값을 사용해서 사용자를 등록 할 수 있다.
```java
 @PostMapping("/users")
    public void createUser(@RequestBody User user){
        User savedUser = service.save(user);


    }
```
- postman에서 이를 실행해서 users를 추가할 수 있음

```json
{
    "name":"New User",
    "joinDate": "2021-01-17T14:47:04.621+00:00"
}
```

### :four: HTTP Status Code 제어

```java
@PostMapping("/users")
    public ResponseEntity<User> createUser(@RequestBody User user){
        User savedUser = service.save(user);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest(). //사용자에게 요청값을 반환해주기 위함
                path("/{id}").
                buildAndExpand(savedUser.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }
```
- 위와 같은 User 보내주면
![http-status](https://user-images.githubusercontent.com/51367515/104848181-2720f500-5927-11eb-84a5-3796fde82b78.PNG)

- 만약 등록되지 않은 ID를 조회한다해도 status 코드는 200Ok가 나온다
![200](https://user-images.githubusercontent.com/51367515/104848298-ac0c0e80-5927-11eb-9c36-827f596564c0.PNG)

- 참조가 되지 않는 ID를 조회해도 OK라고 나온 것은 오류를 점검하기에 맞지 않으므로 exception handling기능을 추가하는 코드를 넣어준다

```java
 @GetMapping("/users/{id}")
    public User retrieveUser(@PathVariable int id){
        User user = service.findOne(id);
        if(user==null){
            throw new UserNotFoundException(String.format("ID[%s} not found",id));
        }
        return user;
    }
```
![오류](https://user-images.githubusercontent.com/51367515/104848616-36a13d80-5929-11eb-8ef4-5b262c23e066.PNG)
- 그외에도 여러가지 오류를 확인할 수 있다.
- 예외처리한 클래스를 클라이언트에게 보냈을 경우 보안상의 문제가 있을수도 있으므로 status code를 변경해야한다.
    - :smile: HTTP Status code
        - 2xx ->OK
        - 4xx -> client의 오류
        - 500 -> Server의 오류
```java
package com.example.restfulwebservice.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
```
![404](https://user-images.githubusercontent.com/51367515/104849281-2a6aaf80-592c-11eb-9241-39be6735927d.PNG

### :five:  AOP를 이용한 Exception Handling
- 일반화된 예외 클라스
```java
package com.example.restfulwebservice.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExceptionResponse {
    private Date timestamp;
    private String message;
    private String details;

}

```
- 공통된 로직을 적용시켜야 하는 경우에 사용하는 것이 'AOP'
- @ControllerAdvice : 모든 컨트롤러가 시행될 때 해당 빈이 사전에 실행됨
- @ExceptionHandler(Exception.class) : class에서 exception이 일어날 때 실행됨

```java
package com.example.restfulwebservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;

@RestController
@ControllerAdvice //모든 컨트롤러가 시행될 때 해당 빈이 사전에 실행됨
public class CustomizedResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request){
        ExceptionResponse exceptionResponse=
                new ExceptionResponse(new Date(), ex.getMessage(), request.getDescription(false));
        return new ResponseEntity(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler(UserNotFoundException.class)
    public final ResponseEntity<Object> handleUserNotFoundException(Exception ex, WebRequest request){
        ExceptionResponse exceptionResponse=
                new ExceptionResponse(new Date(), ex.getMessage(), request.getDescription(false));
        return new ResponseEntity(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

```
- ![오류](https://user-images.githubusercontent.com/51367515/104848616-36a13d80-5929-11eb-8ef4-5b262c23e066.PNG)

### :six:  사용자 삭제를 위한 API 구현
-service에 delete관련 메소드 추가
```java
public User deleteById(int id){
            Iterator<User> iterator= users.iterator();
            while(iterator.hasNext()){
                User user=iterator.next();
                if(user.getId()==id){
                    iterator.remove();
                    return user;

                }

            }
            return null;
        }
```

- 이를 다루는 controller추가
```java
@DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable int id){
        User user = service.deleteById(id);

        if (user==null){
            throw new UserNotFoundException(String.format("ID[%s} not found",id));
        }
    }
```
- Rest의 단점: HTTP가 지원하는 모든메소드 타입을 지원하지 않고, GET, POST, PUT, DELETE만 지원한다.


## :tulip: Restful service 기능 확장
### :one: 유효성 체크를 위한 validation API 사용
- hibernat를 사용해서 코딩-> 이를 위해 dependency에 hibernate추가
```xml
<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-validation</artifactId>
</dependency>
```

- User 클래스에 제약조건 추가
```java
@Data
@AllArgsConstructor
public class User {
    private Integer id;
    @Size(min=2)
    private String name;
    @Past()
    private Date joinDate;
}

```
- @Valid annotation을 추가해주면 위의 valid를 추가해 검사할 수 있다.
- 위와 같은 설정 후 2개 이하의 이름을 Post한다면 오류가 발생한다
![valid404](https://user-images.githubusercontent.com/51367515/104899918-c7762880-59be-11eb-95ad-0403d4864a0d.PNG)

- 에러 감지 클래스에 오버라이딩하는 에러 메소드 추가
```java
@Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ExceptionResponse exceptionResponse= new ExceptionResponse(new Date(),
                ex.getMessage(), ex.getBindingResult().toString());
        return new ResponseEntity(exceptionResponse, HttpStatus.BAD_REQUEST);
    }
```


### :two: 다국어 처리
- 언어 설정에 따라, 적절한 언어를 출력시켜 주는 기능

- @Bean annotation을 등록하면 스프링부트가 시작할 때 IOC 컨테이너에 올린다. 여기서 한국어를 설정해주는 메소드를 추가한다.

```java
@Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ExceptionResponse exceptionResponse= new ExceptionResponse(new Date(),
                "Validation Failed", ex.getBindingResult().toString());
        return new ResponseEntity(exceptionResponse, HttpStatus.BAD_REQUEST);
    }

```
- application.yml에 우리가 사용할 다국적 파일을 지정해준다

```yml
spring:
  messages:
    basename: messages
```
- messages properties에 각각 greeting.messages={인사}를 한국말 버전, 프랑스어 버전, 영어 버전을 만들어준 후 실행한다.
- HelloWorld Controller에 이를 출력하는 메소드 추가
```java
@GetMapping(path ="/hello-world-internationalized")
    public String helloWorldInternationalized(
            @RequestHeader(name="Accept-Language", required=false) Locale locale){
        return messageSource.getMessage("greeting.message", null, locale);
    }
```
![다국적postman](https://user-images.githubusercontent.com/51367515/104917939-6c046480-59d7-11eb-8b16-bc56976fe267.PNG)
### :three: Response 데이터 형식 변환 - XML format
- json 파일 외에 xml 파일로 리턴시켜주기 위해 dependency를 설정함 
```xml
<dependency>
			<groupId>com.fasterxml.jackson.dataformat</groupId>
			<artifactId>jackson-dataformat-xml</artifactId>
			<version>2.10.2</version>


		</dependency>
```

### :four: Response 제어를 위한 필터링
- 클라이언트에게 제공하고자 하는 정보 중 일부를 제외하는 방법(보안상의 이유 등)

- password, ssn 값을 User 변수에 추가한 후 이를 클라이언트에게 보이지 않고 숨기기

- User Class 변경

```java
@Data
@AllArgsConstructor
public class User {
    private Integer id;
    @Size(min=2, message="이름은 두글자 이상 입력해주세요")
    private String name;
    @Past()
    private Date joinDate;

    private String password;
    private String ssn;

}
```

- UserDaoService 변경
```java
static {
        users.add(new User(1, "Kenneth", new Date(),"pass1","701010-1111111"));
        users.add(new User(2, "Alice", new Date(), "pass2","801010-1111111"));
        users.add(new User(3, "Elena", new Date(),"pass3","901010-1111111"));
    }
```

- 여러가지 방법으로 감출 수 있지만 클라이언트에게 필드명은 감출 수 없다

- User Domian에 @JsonIgnore을 붙여 감출 수 있다
```java
    @JsonIgnore
    private String password;
    @JsonIgnore
    private String ssn;
```

### :five: 개별 사용자 조회

- client이외에도 관리자가 서비스를 이용할 수 있는 클래스를 만들겠다. client는 User 정보에 다가갈 수 없지만 관리자는 비밀번호를 제한 모든 User 객체의 정보를 볼 수 있다.
- User 도메인에 
- AdminUserController에 @JsonFilter("UserInfo")를 걸어서 filter 역할을 할 수 있게 한다. 
```java
package com.example.restfulwebservice.user;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/admin")//모든 메소드앞에 이 값을 붙여야함
public class AdminUserController {
    private UserDaoService service;

    public AdminUserController(UserDaoService service) {
        this.service=service;
    }

    @GetMapping("/users")
    public List<User> retrieveAllUsers(){
        return service.findAll();
    }

    @GetMapping("/users/{id}")
    public MappingJacksonValue retrieveUser(@PathVariable int id){
        User user = service.findOne(id);
        if(user==null){
            throw new UserNotFoundException(String.format("ID[%s} not found",id));
        }

        SimpleBeanPropertyFilter filter= SimpleBeanPropertyFilter.
                filterOutAllExcept("id","name","joinDate","ssn");
        FilterProvider filters = new SimpleFilterProvider().addFilter("UserInfo",filter);

        MappingJacksonValue mapping = new MappingJacksonValue(user);
        mapping.setFilters(filters);
        return mapping;
    }


}

```

-  위의 필터를 적용하고 users로 id 가 1인 객체를 참조할 수 없습니다.
![filter1](https://user-images.githubusercontent.com/51367515/104933030-ba236300-59eb-11eb-8c3b-f8805605c916.PNG)

- admin/users를 사용하면 "id","name","joinDate","ssn"를 참조할 수 있다.

![filter2](https://user-images.githubusercontent.com/51367515/104933599-75e49280-59ec-11eb-8070-6ad861ebae8a.PNG)

### :five: 전체 사용자 조회
- AdminUserController에서 전체 조회하는 메서드를 위와 같이 수정한다.
```java
@GetMapping("/users")
    public MappingJacksonValue retrieveAllUsers(){
        List<User> users=service.findAll();

        SimpleBeanPropertyFilter filter= SimpleBeanPropertyFilter.
                filterOutAllExcept("id","name","joinDate","password");
        FilterProvider filters = new SimpleFilterProvider().addFilter("UserInfo",filter);

        MappingJacksonValue mapping = new MappingJacksonValue(users);
        mapping.setFilters(filters);
    }
```

### :six: URI를 이용한 REST API Version 관리


- 페이스북이나 카카오톡 같은 서비스는 사용자에게 API를 공개하고 있다. 이런 API는version 값을 포함하고 있다. 이는 사용자에게 올바른 가이드를 제공해준다.

- AdminUserController에서 user를 검색하는 메소드를 v1,v2로 나누어서 설정
```java
@GetMapping("/v1/users/{id}")
    public MappingJacksonValue retrieveUserV1(@PathVariable int id){
        User user = service.findOne(id);
        if(user==null){
            throw new UserNotFoundException(String.format("ID[%s} not found",id));
        }

        SimpleBeanPropertyFilter filter= SimpleBeanPropertyFilter.
                filterOutAllExcept("id","name","joinDate","ssn");
        FilterProvider filters = new SimpleFilterProvider().addFilter("UserInfo",filter);

        MappingJacksonValue mapping = new MappingJacksonValue(user);
        mapping.setFilters(filters);
        return mapping;
    }

    @GetMapping("/v2/users/{id}")
    public MappingJacksonValue retrieveUserV2(@PathVariable int id){
        User user = service.findOne(id);
        if(user==null){
            throw new UserNotFoundException(String.format("ID[%s} not found",id));
        }
        // User-> User2
        UserV2 userV2  = new UserV2();
        BeanUtils.copyProperties(user, userV2);
        userV2.setGrade("VIP");
        SimpleBeanPropertyFilter filter= SimpleBeanPropertyFilter.
                filterOutAllExcept("id","name","joinDate","grade");
        FilterProvider filters = new SimpleFilterProvider().addFilter("UserInfoV2",filter);

        MappingJacksonValue mapping = new MappingJacksonValue(userV2);
        mapping.setFilters(filters);
        return mapping;
    }
```
- :smile: BeanUtils: 빈들간의 관련된 작업을 도와주는 클래스
- v2메서드에서 UserV2클래스를 생성한 후 원래 User에 있었던 값을 입력해준다. 추가로 UserV2에만 있는 변수인 grade도 "VIP"라는 값을 입력해준다.
- V1 실행
![v1](https://user-images.githubusercontent.com/51367515/104940152-afb99700-59f4-11eb-849f-bd0248cfe87b.PNG)
- V2 실행
![v2](https://user-images.githubusercontent.com/51367515/104940158-b0eac400-59f4-11eb-9296-8b91589d5d9f.PNG)

### :seven: Request Parameter와 Header를 이용한 API Version 관리

- parameter를 사용해서 버전 관리 하는법-> 인수로 파라미터를 설정
    - 실행화면
![version1](https://user-images.githubusercontent.com/51367515/104940955-cd3b3080-59f5-11eb-9bfc-6a8852409091.PNG)
```java
@GetMapping(value ="/users/{id}/", params ="version=1")
@GetMapping(value= "/users/{id}/",params="version=2")
```
- header값을 위한 version 관리
    - 실행화면
    ![header-version](https://user-images.githubusercontent.com/51367515/104942087-52731500-59f7-11eb-93b5-a0beb3eac194.PNG)
```java
@GetMapping(value= "/users/{id}", headers="X-API-VERSION=1")
@GetMapping(value= "/users/{id}", headers="X-API-VERSION=2")
```

- mine time을 위한 version관리
    - 실행화면
    ![app1-version](https://user-images.githubusercontent.com/51367515/104942388-bac1f680-59f7-11eb-98b2-5c7aee93bebc.PNG)
```java
@GetMapping(value="/users/{id}", produces="application/vnd.company.appv1+json")
@GetMapping(value="/users/{id}", produces="application/vnd.company.appv2+json")
```
- :smile: version관리는 단순하게 사용자에게 보여지는 항목을 제하는 용도가 아니라 설계나 구조가 변경될 때 버전을 변경하기 위해서 사용한다. 또한 사용자에게는 상황에 맞게 어떠한 version을 사용하는 지 가이드를 해줘야한다.
![version](https://user-images.githubusercontent.com/51367515/104942689-21471480-59f8-11eb-829e-0307d223b458.PNG)
- :smile: Factor
    - URI값이 너무 지저분한건 피하자
    - 잘못된 헤더값을 사용하지 말자
    - caching으로 인해 지정한 값이 반영되지 않는 경우를 고려 -> 안 될 경우 캐시를 삭제하기!
    - 적절한 개발 도구가 제공되어야 한다.
