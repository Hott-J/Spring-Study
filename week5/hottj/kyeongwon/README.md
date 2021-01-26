## :cherry_blossom: Spring Boot API 사용

### 1. Level3 단계의 REST API 구현을 위한 HATEOAS 적용
* REST API 성숙도 (총 4단계)
<img src="https://user-images.githubusercontent.com/61045469/105633360-6d340680-5e9b-11eb-93c7-49c08b41febc.png" width="60%" height="40%"></img><br/>

* HATEOAS(Hypermedia as the Engine of Application State)
  * 현재 리소스와 연관된(호출 가능한) 자원 상태 정보를 제공
  
* pom.xml에 추가(spring boot version : 2.3.7.RELEASE)
```java
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-hateoas</artifactId>
		</dependency>
```

* UserController에 추가
```java
    @GetMapping("/users/{id}")
    public EntityModel<User> retrieveUser(@PathVariable int id) { // 받을때 int 형으로 받으면 String -> int 자동 형변환됨
        User user = service.findOne(id);

        // HTTP Status Code 제어 -> Exception Handling
        if(user == null) {
            throw new UserNotFoundException(String.format("ID[%s] not found", id));
        }

        // HATEOAS
        EntityModel<User> model = new EntityModel<>(user);
        WebMvcLinkBuilder linkTo = linkTo(methodOn(this.getClass()).retrieveAllUsers());
        model.add(linkTo.withRel("all-users"));

        return model;
    }
```

* **HATEOAS 사용 이유**
  * RESTful API를 사용하는 클라이언트가 서버와 동적인 상호작용이 가능하도록 하여 편리한 기능 제공
  * 즉, 클라이언트가 서버로부터 어떠한 요청을 할 때, 요청에 필요한(의존되는) URI를 동적으로 응답에 포함시켜 반환
  * 각 기능마다 URI를 링크 시킴으로써, 동적인 API 제공이 가능하도록 한다.
  * ex) Postman에서 http://localhost:8088/users/1을 전송했을 경우, all-users에 대한 정보는 http://localhost:8088/user임을 알려준다.
  <img src="https://user-images.githubusercontent.com/61045469/105673144-f34a5e80-5f28-11eb-99d4-6b31b5133e7f.png" width="70%" height="50%"></img><br/>

### 2. REST API Documentation을 위한 Swagger 사용
* 개발자 도움말 페이지 생성하는 기능

* pom.xml에 추가(spring boot version : 2.3.7.RELEASE)
```java
		<dependency>
			<groupId>io.springfox</groupId>
			<artifactId>springfox-swagger2</artifactId>
      <version>2.9.2</version>
		</dependency>
    <dependency>
			<groupId>io.springfox</groupId>
			<artifactId>springfox-swagger-ui</artifactId>
      <version>2.9.2</version>
		</dependency>
```

* Swagger Documentation Customizing
  * Swagger Config class 추가
```java
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    // Contact 객체
    private static final Contact DEFAULT_CONTACT = new Contact("Kenneth Lee",
            "http://www.joneconsulting.co.kr", "edowon@joneconsulting.co.kr");

    // API 정보
    private static final ApiInfo DEFAULT_API_INFO = new ApiInfo("Awesome API Title",
            "My User management REST API service", "1.0", "urn:tos", DEFAULT_CONTACT,
            "Apache 2.0", "http://www.apache.org/license/LICENSE-2.0", new ArrayList<>());

    private static final Set<String> DEFAULT_PRODUCES_AND_CONSUMES = new HashSet<>(
            Arrays.asList("application/json", "application/xml"));

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(DEFAULT_API_INFO)
                .produces(DEFAULT_PRODUCES_AND_CONSUMES)
                .consumes(DEFAULT_PRODUCES_AND_CONSUMES);
    }

    @Bean
    public LinkDiscoverers discoverers() {
        List<LinkDiscoverer> plugins = new ArrayList<>();
        plugins.add(new CollectionJsonLinkDiscoverer());
        return new LinkDiscoverers(SimplePluginRegistry.create(plugins));
    }
}
```

* User Domain 객체에 필드별로 documentation을 사용할 수 있는 설명 추가
  * class 위에 작성
  ```java
  @ApiModel(description = "사용자 상세 정보를 위한 도메인 객체")
  ```

  * 필드에 작성
  ```java
  @ApiModelProperty(notes = "사용자 이름을 입력해 주세요.")
  ```

### 3. REST API Monitoring을 위한 Actuator 설정
* 서버 monitoring 도구

* pom.xml에 추가(spring boot version : 2.3.7.RELEASE)
```java
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-actuator</artifactId>
		</dependency>
```

* application.yml에 추가
```java
management:
  endpoints:
    web:
      exposure:
        include: "*"
```

### 4. HAL Browser를 이용한 HATEOAS 기능 구현
* HAL Browser
  * HAL : Hypertext Application Language
  