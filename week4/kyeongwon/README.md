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
* REST API가 지원하는 method : GET, POST, PUT(등록된 data를 변경할 경우에 사용), DELETE
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
* **@AllArgsConstructor**는 생성자를 만들어주는 역할을 하기 때문에 따로 생성자 코드를 추가하면 error가 난다. 모든 필드를 가진 생성자를 자동으로 생성해준다.
* **@NoArgsConstructor**는 매개변수가 없는 생성자를 만들때 사용한다. (Default 생성자)
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
@Service
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
    
    // 사용자 삭제
    public User deleteById(int id) {
        Iterator<User> iterator = users.iterator();

        while (iterator.hasNext()) {
            User user = iterator.next();

            if (user.getId() == id) {
                iterator.remove();
                return user;
            }
        }

        return null;
    }
}
```

### 2. User API 구현
* 사용자 목록 조회 -> GET HTTP Method
* 사용자 등록 -> POST HTTP Method
* ctrl + alt + v : 변수 자동 생성 단축키

* UserController 클래스 (HTTP Status Code 제어 )
```java
@RestController
public class UserController {
    private UserDaoService service;

    public UserController(UserDaoService service) {
        this.service = service;
    }

    @GetMapping("/users")
    public List<User> retrieveAllUsers() {
        return service.findAll();
    }

    // GET /users/1 or /users/10 -> String 형으로 서버에 전달됨
    @GetMapping("/users/{id}")
    public User retrieveUser(@PathVariable int id) { // 받을때 int 형으로 받으면 String -> int 자동 형변환됨
        User user = service.findOne(id);

        // HTTP Status Code 제어 -> Exception Handling
        if(user == null) {
            throw new UserNotFoundException(String.format("ID[%s] not found", id));
        }

        return user;
    }

    @PostMapping("/users")
    public ResponseEntity<User> createUser(@RequestBody User user) { // object 형식이기 때문에 @RequestBody 사용
        User savedUser = service.save(user);

        // HTTP Status Code 제어 -> 좋은 API 설계 방법.(네트워크 트래픽 감소, 효율적!) 최종적으로 200 대신 201 created 출력
        URI location = ServletUriComponentsBuilder.fromCurrentRequest() // 현재 요청된 request 값을 사용한다는 것을 의미
                .path("/{id}")
                .buildAndExpand(savedUser.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }
    
    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable int id) {
        User user = service.deleteById(id);

        if (user == null) { // 삭제하려는 사용자가 존재하지 않을 경우
            throw new UserNotFoundException(String.format("ID[%s] not found", id));
        }
    }
}
```

* Exception 클래스 -> null값을 리턴하지 않고 오류를 발생시키게끔 하였다.

* 또한 500 error code를 리턴할 경우 예외 발생 원인코드가 드러나 보안상의 문제가 있을 수 있으므로, 다른 적절한 상태로 개선해보도록 한다.
```java
// HTTP Status Code
// 2XX -> OK
// 4XX -> Client 문제
// 5XX -> Server 문제
// resource 없을 경우 500 error code 대신 404 not found 를 리턴하도록 하자!
@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
```

### 3. Spring AOP를 이용한 Exception Handling
* 다른 클래스에도 exception을 적용할 수 있도록 exception폴더 생성
* 일반화된 exception 클래스 생성 -> **AOP**

* ExceptionResponse 클래스
```java
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExceptionResponse {
    private Date timestamp;
    private String message;
    private String details;
}
```

* CustomizedResponseEntityExceptionHandler 클래스
```java
@RestController
@ControllerAdvice // 모든 컨트롤러가 실행될 때마다 적용 -> 에러 생기면 ExceptionHandler 에 등록된 method 실행됨
public class CustomizedResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request) {
        ExceptionResponse exceptionResponse =
                new ExceptionResponse(new Date(), ex.getMessage(), request.getDescription(false));

        return new ResponseEntity(exceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // UserNotFoundException 일어날 경우 실행되는 method
    @ExceptionHandler(UserNotFoundException.class)
    public final ResponseEntity<Object> handleUserNotFoundException(Exception ex, WebRequest request) {
        ExceptionResponse exceptionResponse =
                new ExceptionResponse(new Date(), ex.getMessage(), request.getDescription(false));

        return new ResponseEntity(exceptionResponse, HttpStatus.NOT_FOUND);
    }
}
```
<br>

## :cherry_blossom: RESTful Service 기능 확장

### 1. Validation API - 유효성 체크
* User Domain에 유효성 추가 - @Size, @Past
```java
    @Size(min=2) // 2글자 이상이여야 한다.
    private String name;

    @Past // 회원이 가입할때에는 과거 데이터만 쓸수 있도록 제약을 걸어둠
    private Date joinDate;
```

* @Valid : 유효성 검사 어노테이션

* handleMethodArgumentNotValid함수 재정의 - CustomizedResponseEntityExceptionHandler에 추가
```java
    // handleMethodArgumentNotValid 함수 재정의 - 오버라이딩
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), ex.getMessage(),
                ex.getBindingResult().toString());
        return new ResponseEntity(exceptionResponse, HttpStatus.BAD_REQUEST);
    }
```

* 이외에도 id값이 반드시 숫자여야 한다던지, 유효성 검사를 더 추가할 수 있다.


### 2. Internationalization 구현 - 다국어 처리
* main class에 추가 - Spring Boot가 초기화될때 메모리에 올라간다.(Bean으로 등록됨)
```java
	@Bean
	public LocaleResolver localeResolver() {
		SessionLocaleResolver localeResolver = new SessionLocaleResolver();
		localeResolver.setDefaultLocale(Locale.KOREA);
		return localeResolver;
	}
```

* 다국어 파일 저장 - application.yml 파일에 추가
```java
spring:
  messages:
    basename: messages
```

* 다국어 파일
  * messages.properties - 한국어 저장
  * messages_fr.properties - 프랑스어 저장
  * messages_en.properties - 영어 저장

* Headers에 Accept-Language를 fr, en으로 하면 각각 프랑스어, 영어로 나옴. 아무것도 안쓰거나 없는걸 쓰면 한국어로 나옴

### 3. Response 데이터 형식 변환 - XML format
* xml 처리 라이브러리 추가 - pom.xml
```java
		<dependency>
			<groupId>com.fastxml.jackson.dataformat</groupId>
			<artifactId>jackson-dataformat-xml</artifactId>
			<version>2.10.2</version>
		</dependency>
```

* Headers에 KEY : Accept, VALUE : application/xml으로 지정하면 xml형태로 반환해준다.
* VALUE를 application/json이면 json형태로 반환받는다.(spring boot가 기본적으로 정해놓은 format은 json)

### 4. Response 데이터 제어를 위한 Filtering
* spring boot에서는 filtering 기능을 제공한다.
* jackson library 사용하면 외부에 노출시키고 싶지 않은 data를 제어할 수 있다.
* User Domain에 password, ssn 추가
```java
@Data
@AllArgsConstructor
@NoArgsConstructor // default 생성자
@JsonIgnoreProperties(value={"password", "ssn"}) // 무시하고 싶은 필드명들 기입
public class User {
    private Integer id;

    @Size(min=2, message="Name은 2글자 이상 입력해 주세요.") // 2글자 이상이여야 한다.
    private String name;

    @Past // 회원이 가입할때에는 과거 데이터만 쓸수 있도록 제약을 걸어둠
    private Date joinDate;

    // @JsonIgnore // filtering - 특정 필드값 제어
    private String password;

    // @JsonIgnore
    private String ssn; // 주민번호
}
```

* UserDaoService 수정
```java
    static {
        users.add(new User(1, "Kenneth", new Date(), "pass1", "701010-1111111"));
        users.add(new User(2, "Alice", new Date(), "pass2", "801010-1111111"));
        users.add(new User(3, "Elena", new Date(), "pass3", "901010-1111111"));
    }
```

* Domain class가 가지고 있는 값 중, 외부에 노출되어서는 안되는 정보(password, ssn)는 숨겨야 한다. -> @JsonIgnoreProperties 또는 @JsonIgnore 사용

### 5. 프로그래밍으로 제어하는 Filtering 방법 - 전체, 개별 사용자 조회
* User Domain 수정
```java
@Data
@AllArgsConstructor
@NoArgsConstructor // default 생성자
@JsonFilter("UserInfo")
public class User {
    private Integer id;

    @Size(min=2, message="Name은 2글자 이상 입력해 주세요.")
    private String name;

    @Past // 회원이 가입할때에는 과거 데이터만 쓸수 있도록 제약을 걸어둠
    private Date joinDate;

    private String password;
    private String ssn; // 주민번호
}
```

* 새로운 Controller 생성 - AdminUserController(관리자를 위한 컨트롤러)
```java
@RestController
@RequestMapping("/admin") // /admin으로 시작하는 모든 요청을 받는다. -> /admin/users를 받으면 getmapping 동작
public class AdminUserController {
    private UserDaoService service;

    public AdminUserController(UserDaoService service) {
        this.service = service;
    }
    
    @GetMapping("/users") // /admin/users
    public MappingJacksonValue retrieveAllUsers() {
        List<User> users = service.findAll();

        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter
                .filterOutAllExcept("id", "name", "joinDate", "ssn");

        FilterProvider filters = new SimpleFilterProvider().addFilter("UserInfo", filter); // filter 이름 -> User에 정의한 대로 써줘야됨

        MappingJacksonValue mapping = new MappingJacksonValue(users);
        mapping.setFilters(filters);

        return mapping;
    }

    // GET /admin/users/1 or /admin/users/10 -> String 형으로 서버에 전달됨
    @GetMapping("/users/{id}") // /admin/users/{id}
    public MappingJacksonValue retrieveUser(@PathVariable int id) {
        User user = service.findOne(id);

        // HTTP Status Code 제어 -> Exception Handling
        if(user == null) {
            throw new UserNotFoundException(String.format("ID[%s] not found", id));
        }

        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter
                .filterOutAllExcept("id", "name", "joinDate", "ssn");

        FilterProvider filters = new SimpleFilterProvider().addFilter("UserInfo", filter); // filter 이름 -> User에 정의한 대로 써줘야됨

        MappingJacksonValue mapping = new MappingJacksonValue(user);
        mapping.setFilters(filters);

        return mapping;
    }
}
```

### 6. URI를 이용한 REST API Version 관리
* version2를 위한 새로운 Domain class 생성 - grade 필드 추가
```java
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonFilter("UserInfoV2")
public class UserV2 extends User { // User class 상속
    private String grade;
}
```

* V1, V2가 전혀 다른 REST API를 가지고 있다.
* 버전 관리가 추가된 AdminUserController
```java
    // GET /admin/users/1 -> /admin/v1/users/1
    @GetMapping("/v1/users/{id}")
    public MappingJacksonValue retrieveUserV1(@PathVariable int id) {
        User user = service.findOne(id);

        // HTTP Status Code 제어 -> Exception Handling
        if(user == null) {
            throw new UserNotFoundException(String.format("ID[%s] not found", id));
        }

        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter
                .filterOutAllExcept("id", "name", "joinDate", "ssn");

        FilterProvider filters = new SimpleFilterProvider().addFilter("UserInfo", filter); // filter 이름 -> User에 정의한 대로 써줘야됨

        MappingJacksonValue mapping = new MappingJacksonValue(user);
        mapping.setFilters(filters);

        return mapping;
    }

    // /admin/v2/users/1
    @GetMapping("/v2/users/{id}")
    public MappingJacksonValue retrieveUserV2(@PathVariable int id) {
        User user = service.findOne(id);

        // HTTP Status Code 제어 -> Exception Handling
        if(user == null) {
            throw new UserNotFoundException(String.format("ID[%s] not found", id));
        }

        // User -> UserV2
        UserV2 userV2 = new UserV2();
        BeanUtils.copyProperties(user, userV2); // BeanUtils: Bean들간의 작업을 도와주는 class. id, class, joinDate, password, ssn
        userV2.setGrade("VIP"); // grade 필드

        SimpleBeanPropertyFilter filter = SimpleBeanPropertyFilter
                .filterOutAllExcept("id", "name", "joinDate", "grade");

        FilterProvider filters = new SimpleFilterProvider().addFilter("UserInfoV2", filter); // filter 이름 -> User에 정의한 대로 써줘야됨

        MappingJacksonValue mapping = new MappingJacksonValue(userV2);
        mapping.setFilters(filters);

        return mapping;
    }
```

### 7. Request Parameter와 Header를 이용한 API Version 관리
* Parameter 방식
  * @GetMapping("/v1/users/{id}") -> @GetMapping(value="/users/{id}/", params="version=1") 로 수정
  * 요청 방식 : localhost:8088/admin/users/1/?version=1 (파라미터라서 앞에 ? 붙여야 한다.)
  
* Header 방식
  * @GetMapping(value="/users/{id}/", params="version=1") -> @GetMapping(value="/users/{id}", headers="X-API-VERSION=1") 로 수정
  * 요청 방식 : localhost:8088/admin/users/1 쓰고 Headers에 KEY : X-API-VERSION, VALUE = 1 or 2 써서 요청
  
* produces 방법(MIME type 지정)
  * @GetMapping(value = "/users/{id}", produces = "application/vnd.company.appv1+json") 로 수정
  * 요청 방식 : localhost:8088/admin/users/1 쓰고 Headers에 KEY : Accept, VALUE = application/vnd.company.appv1+json 써서 요청
  
* Version 관리 이유
  * 단순히 사용자에게 보여주는 항목을 제한하는 용도라기 보단, REST API설계가 변경되거나 application구조가 바뀔 때도 version을 변경한다.
  * 사용자에게 어떤 API version을 사용해야 하는지에 대해 가이드를 명시해 주어야 한다.

* URI, Parameter를 통한 버전 관리 -> 일반 브라우저에서 실행 **가능**
* MIME type, Header를 통한 버전 관리 -> 일반 브라우저에서 실행 **불가능**
