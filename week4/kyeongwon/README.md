## :cherry_blossom: Web Service & Web Application

### 1. Web Service
* Web Service : 네트워크 상에서 서로 다른 종류의 컴퓨터들 간에 상호작용을 하기 위한 소프트웨어 시스템

### 2. Web Application
* Web Application : 인터넷이나 인트라넷을 통해 웹 브라우저에서 이용할 수 있는 응용 소프트웨어
* Web Application 구조   
<img src="https://user-images.githubusercontent.com/61045469/104447786-9b822e00-55df-11eb-89c0-ce5f89e7c006.PNG" width="70%" height="50%"></img><br/>

### 3. Web Service 개발 - SOAP & RESTful
* **SOAP(Simple Object Access Protocol)**
  * HTTP, HTTPS 프로토콜 위에서 XML request, response를 처리하기 위한 서비스 시스템
  * 복잡한 구조를 갖고 있고, 오버헤드가 커서 개발하기 어렵다. -> 요즘에는 RESTful을 많이 쓰게 되었다.

* REST(Representational State Transfer)
  * Resource의 Representation에 의한 상태(컴퓨터가 가지고 있는 자원-파일, 데이터 등) 전달
  * HTTP Method를 통해 Resource를 처리하기 위한 아키텍쳐
  
* **RESTful**
  * REST API를 제공하는 웹 서비스
  * RESTful Service를 사용하기 위해서는 HTTP Protocol을 사용할 수 있는 Application이 필요하다. 일반적으로 Web browser를 사용하며, 개발자들은 Postman, curl을 사용하기도 한다.
  * HTTP Method : HTTP protocol을 통해서 client가 server에 전달하게 되는 목적, 종류를 알려주는 수단 (data를 보낼 때 POST method 사용)
  * 모든 HTTP request는 server로부터 처리된 후, 응답 코드(HTTP Status code)와 함께 처리된 결과를 반환한다. (code가 200이면 정상으로 처리됨을 의미)   
<img src="https://user-images.githubusercontent.com/61045469/104449031-53640b00-55e1-11eb-9fcb-27f4668d4104.png" width="70%" height="50%"></img><br/>

* Resource
  * URI(Uniform Resource Identifier), 인터넷 자원을 나타내는 고유한(유일한) 주소
  * Resource 요청, 응답할 때 XML, HTML, JSON과 같은 문서 포맷을 사용한다.
  
### 4. SOAP vs RESTful
<img src="https://user-images.githubusercontent.com/61045469/104450801-003f8780-55e4-11eb-9f82-7c610de59eb4.png" width="60%" height="40%"></img><br/>

<br>

## :cherry_blossom: Spring Boot로 개발하는 RESTful Service

### 1. Spring Boot 개요
* Spring Boot
  * 단독으로 실행이 가능한 스프링 애플리케이션을 생성한다. 
  * Tomcat, Jetty, Undertow 등을 내장하고 있다.
  * Spring Framework에 필요한 다양한 설정(IoC, AOP 등) 작업이 많이 생략되었다. -> 개발자는 비즈니스 로직에 더 집중할 수 있다.
  * @SpringBootApplication가 붙여진 main 클래스를 실행시키면 Application이 실행된다.
  
### 2. REST API 설계
* Social Media Application
  * User -> Posts   
  <img src="https://user-images.githubusercontent.com/61045469/104457293-31708580-55ed-11eb-80c1-325db2fbff49.png" width="60%" height="40%"></img><br/>

### 3. Spring Boot Project 생성
* [https://start.spring.io](https://start.spring.io/) 사이트 접속하여 프로젝트 생성
  * Project : Maven Project
  * Language : Java (jdk 13.0.2)
  * Spring Boot : 2.4.1
  * Project Metadata
    * Group : com.example
    * Artifact : restful-web-service
    * Packaging : Jar
    * Java : 8
  * Dependencies : Spring Boot DevTools, Lombok, Spring Web, Spring Data JPA, H2 Database 추가
  
### 4. Spring Boot Project 구조 확인과 실행 방법
* application.properties 파일명을 application.yml로 수정 -> Spring 설정 파일(yml이 더 많이 사용되는 추세이다.)
* application.yml 파일에 추가(톰캣 서버 포트 설정)
```java
server:
  port: 8088
```
* application.yml vs application.properties
  * application.properties 형식 -> 설정이름=값
  * application.yml 형식 -> 설정이름:값

### 5. HelloWorld Controller 추가
* 일반 controller class와 REST controller class는 다르다. -> @RestControlller 사용
```java
@RestController
public class HelloWorldController {
    // GET
    // /hello-world (endpoint)
    // 예전 방식 : @RequestMapping(method=RequestMethod.GET, path="/hello-world")
    @GetMapping("/hello-world")
    public String helloWorld() {
        return "Hello World";
    }
}
```
* Postman으로도 확인 가능  
<img src="https://user-images.githubusercontent.com/61045469/104487101-0186a980-5610-11eb-8b03-8313e68529ed.png" width="70%" height="50%"></img><br/>

### 6. HelloWorld Bean 추가
* HelloWorldController에 추가
  * String형식이 아닌 Bean객체 형식으로 return하기 때문에 Spring Framework에서는 **json형태**({"message":"Hello World"})로 변환하여 반환해준다.
  * 여기서 생긴 의문! @ResponseBody이 없는데 왜 ViewResolver가 동작하지 않고 HttpMessageConverter가 동작할까?
    * HelloWorldController에 있는 @RestController에 @ResponseBody가 포함되어 있었다~
```java
    // class 생성 : alt + enter
    @GetMapping("/hello-world-bean")
    public HelloWorldBean helloWorldBean() {
        return new HelloWorldBean("Hello World");
    }
```
* Lombok 실행하기 위한 옵션 설정
  * IntelliJ - File - Settings - annotation 검색(Annotation Processors) - Enable annotation processing 체크 후 Apply
* Lombok Plugin 추가
  * IntelliJ - File - Settings - Plugins - lombok 검색 후 Lombok Plugin 설치 - Restart IDE
* **@Data** : Lombok에서는 여러 method들을 자동 생성해주기 때문에 getter, setter를 만들 필요 없다.
* **@AllArgsConstructor**는 생성자를 만들어주는 역할을 하기 때문에 따로 생성자 코드를 추가하면 error가 난다.
* 매개변수가 없는 생성자를 만들어주고 싶은 경우, **@NoArgsConstructor**를 쓰면 된다.
```java
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HelloWorldBean {
    private String message;

}
```

### 7. DispatcherServlet과 프로젝트 동작의 이해
* DispatcherServlet
  * client 요청을 처리하는 gateway로 볼 수 있다.
  * client 요청을 한곳으로 받아서 처리하고 요청에 맞는 Handler로 요청을 전달한다.
  * Handler 실행 결과를 Http response 형태로 만들어서 반환한다.   
<img src="https://user-images.githubusercontent.com/61045469/104559350-9a0c4080-5687-11eb-8bb2-83e355ff684e.png" width="60%" height="40%"></img><br/>
* RestController
  * Spring 4부터 @RestController 지원
  * @Controller + @RestController 기능 모두 포함
  * View를 갖지 않는 REST Data(JSON, XML)를 반환    
<img src="https://user-images.githubusercontent.com/61045469/104559637-0129f500-5688-11eb-91b7-cafa709f76fd.png" width="70%" height="50%"></img><br/>

### 8. Path Variable 사용
* 가변 data값 : @PathVariable annotation 사용
* HelloWorldController에 추가
```java
    @GetMapping("/hello-world-bean/path-variable/{name}")
    public HelloWorldBean helloWorldBean(@PathVariable String name) {
        return new HelloWorldBean(String.format("Hello World, %s", name));
    }
```
* JSON 플러그인 설치
  * chrome 웹 스토어 - JSON Viewer 설치하기
  
<br>

## :cherry_blossom: User Service API 구현

### 1. User 도메인 클래스 생성
* User Domain 생성
```java
@Data
@AllArgsConstructor
public class User {
    private Integer id;
    private String name;
    private Date joinDate;
}
```
* User Service 생성
```java
// 비즈니스 로직
public class UserDaoService {
    private static List<User> users = new ArrayList<>();

    private static int usersCount = 3;

    static {
        users.add(new User(1, "Kenneth", new Date()));
        users.add(new User(2, "Alice", new Date()));
        users.add(new User(3, "Elena", new Date()));
    }

    public List<User> findAll() {
        return users;
    }

    public User save(User user) {
        if (user.getId() == null) {
            user.setId(++usersCount);
        }

        users.add(user);
        return user;
    }

    public User findOne(int id) {
        for (User user : users) {
            if (user.getId() == id) return user;
        }
        return null;
    }
}
```
