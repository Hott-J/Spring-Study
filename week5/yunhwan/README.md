# REST API Level3 위한 HATEOAS 설정

## HATEOS (Hypermedia As The Engine Of Application State)

- REST 아키텍처의 한 구성요소
- HATEOS를 통해 어플리케이션 상태 전이 메커니즘 제공 가능
- 현재 리소스와 연관된 자원 상태 정보 제공 (link 제공)

하나의 리소스에서 사용할 수 있는 추가적인 작업 정보 제공 가능

![README%20md%20ba4c33a880bb41809acbf0d18c4a918d/_2021-01-03__1.28.38.png](README%20md%20ba4c33a880bb41809acbf0d18c4a918d/_2021-01-03__1.28.38.png)

0 → 단순히 컴퓨터가 가지고 있는 자원 전달

- 모든 전송, 응답 POST // 접근 가능한 엔드포인트는 **하나** // Body에 정보를 넣어 전송

1 → 리소스 안의 1단계

- REST 도입, 고유의 URI를 통해 자원 제공, 모든 자원 제공 (Json, XML)

2 → HTTP 상태 이용

- HTTP method 활용, 어떻게 요청하든 같은 응답

3 → 리소스와 연관된 자원 상태 정보

- 다음 작업을 할수 있는 URL을 링크로 같이 보냄, Lv3의 URL 변경 시 단점 해결

- UserController

    ```java
    		@GetMapping("/users/{id}")
        public EntityModel<User> retrieveUser(@PathVariable int id) {
            User user = service.findOne(id); // command + option + v 사용

            if (user == null) { // 유저가 존재하지 않다면... 오류처리
                throw new UserNotFoundException(String.format("ID[%s] not found", id));
            }

            // HATEOUS 관련 코드 추가
            EntityModel<User> model = new EntityModel<>(user);
            WebMvcLinkBuilder linkTo = linkTo(methodOn(this.getClass()).retrieveAllUsers()
            );
            model.add(linkTo.withRel("all-users")); // retriveAllUser를 => all-users uri에 링크
            return model;
            // return service.findOne(id) => Data가 없으면 NULL을 반환할 뿐 오류 코드를 반환하지는 않는다.
        }
    ```

- GET Method 사용시 반환 값

    ![README%20md%20ba4c33a880bb41809acbf0d18c4a918d/_2021-01-03__1.53.47.png](README%20md%20ba4c33a880bb41809acbf0d18c4a918d/_2021-01-03__1.53.47.png)

    Hateous를 통해 현재 자원과 연관된 **추가 작업의 정보**를 함께 제공한다.

    개발자가 할일이 많아지지만 더 많은 정보를 사용자에게 제공해줄 수 있다.

- HATEOS dependency 추가

```java
			<dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-hateoas</artifactId>
        </dependency>
```

# REST API  DOC Swagger 설정 (개발자 Documentation)

- pom.xml 추가

swagger 의존성

```xml
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

- swagger 설정을 위한 클래스 추가
    - config 패키지 SwaggerConfig 클래스

```java
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    //@Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2);
    }
}
```

- 추가 정보 설정

```java
@Configuration
@EnableSwagger2
public class SwaggerConfig {
    // 컨택트와 API정보
    private static final Contact DEFAULT_CONTACT = new Contact("Kenneth Lee",
            "http://www.joneconsulting.co.kr", "park@aaa.co.kr");
    private static final ApiInfo DEFAULT_API_INFO = new ApiInfo("Awesome park API",
            "My User Management REST API Service", "1.0", "urn:tos",
            DEFAULT_CONTACT, "Apache 2.0",
            "http://www.apache.org/licenses/LINCENSE-2.0", new ArrayList<>());
    private static final Set<String> DEFAULT_PRODUCES_AND_CONSUMES = new HashSet<>(
            Arrays.asList("application/json", "application/xml")
    );
    //@Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(DEFAULT_API_INFO)
                .apiInfo(DEFAULT_API_INFO)
                .produces(DEFAULT_PRODUCES_AND_CONSUMES)
                .consumes(DEFAULT_PRODUCES_AND_CONSUMES);
    }
}
```

- API JSON 반환 값을 Swagger가 보기 좋게 만들어줌

![README%20md%20ba4c33a880bb41809acbf0d18c4a918d/_2021-01-03__2.42.08.png](README%20md%20ba4c33a880bb41809acbf0d18c4a918d/_2021-01-03__2.42.08.png)

- Swagger 추가 정보 설정
    - SwaggerConfig 클래스에 기본 개발자 정보, API 정보, 라이센스 등을 설정할 수 있다.
    - 또한 User객체의 필드에 대해서
    `@ApiModelPropertiy` 를 이용해서 description을 추가할 수 있다.

        ```java

            @ApiModelProperty(notes = "사용자 이름을 입력해주세요.")
            private String name;

            @ApiModelProperty(notes = "사용자의 등록일을 입력해주세요.")
            private Date joinDate;

            @ApiModelProperty(notes = "사용자의 비밀번호를 입력해주세요.")
            private String password;

            @ApiModelProperty(notes = "사용자의 주민번호를 입력해주세요.")
            private String ssn;
        ```

        - swagger-ui ⇒ User 도메인 객체

        ![README%20md%20ba4c33a880bb41809acbf0d18c4a918d/_2021-01-03__2.58.42.png](README%20md%20ba4c33a880bb41809acbf0d18c4a918d/_2021-01-03__2.58.42.png)

# REST API Monitoring Actuator 설정

> 스프링 부트 프로젝트에 모니터링 기능을 추가

- pom.xml

```
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

- 서버 재실행 시 기동하고 있는 어플리케이션의 상태 정보를 알수 있다. 8088/actuator
- 액츄에이터도 다른 작업 정보를 제공하는 헤테우스를 가짐
- /actuator/health ⇒ 서버 작동 정보

- 더 많은 정보를 얻기 위해서 application.yml 수정
    - 추가 시 그냥 입력이 아닌 추천 메서드에서 선택

```java
management:
  endpoint:
    web:
      exposure:
        include: "*"
```

더 많은 정보를 확인해볼수 있다. (서버의 모니터링 도구)

# HAL  Browser

`response Body`에 `부가적인 정보`를 추가하여 제공

- API간 쉬운 검색 가능
- 메타 정보를 하이퍼링크 형태로 가짐
- 부가적으로 사용할 수 있는 정보를 하이퍼텍스트로 제공

```xml
<dependency>
   <groupId>org.springframework.data</groupId>
   <artifactId>spring-data-rest-hal-browser</artifactId>
   <version>3.3.6.RELEASE</version>
</dependency>
```

현재 버전문제로 실행불가 (따로 만들어야함)

root 접속 시 HAL브라우저 정보가 나옴

자동으로 더 사용할 수 있는 링크 정보를 쉽게 알수 있다는 장점이 있다.

# Spring Security (보안)

## Spring Security를 이용한 인증 처리

누구나 접속가능한 것이 아닌 인증이 필요한 Restful API에 사용

방법 : oauth, jwp, pw 등

- 서버 재구동 시 패스워드 생성됨

![README%20md%20ba4c33a880bb41809acbf0d18c4a918d/_2021-01-07__3.08.45.png](README%20md%20ba4c33a880bb41809acbf0d18c4a918d/_2021-01-07__3.08.45.png)

- [port num]/users/1 ( 특정 유저 정보 추출 )
    - 권한 없음

    ![README%20md%20ba4c33a880bb41809acbf0d18c4a918d/_2021-01-07__3.10.37.png](README%20md%20ba4c33a880bb41809acbf0d18c4a918d/_2021-01-07__3.10.37.png)

- 헤더의 Authorization의 Basic Auth타입에 패스워드를 인증 정보로 실어서 보내면 유저 정보를 리턴받을 수 있다.

![README%20md%20ba4c33a880bb41809acbf0d18c4a918d/_2021-01-07__3.14.02.png](README%20md%20ba4c33a880bb41809acbf0d18c4a918d/_2021-01-07__3.14.02.png)

## 개발자가 직접 지정한 pw를 사용하기

- application.yml 추가

    ```yaml
    spring:
      messages:
        basename: messages
      security:
        user:
          name: yunhwan
          password: mypassword
    ```

    인증 정보에 설정한 user와 pw를 입력해야 정상적으로 값을 반환해준다. (한번 인증 후에는 쿠기가 남아있을 수 있다)

yml 파일은 유저나 암호가 바뀔 때마다 코드를 수정해야하므로 좋지 않다.

따라서, 정적이 아닌 동적으로 변할 수 있는 방법을 사용해야 한다.

- SecurityConfig 파일 추가를 통한 동적 권한 설정

    ```java
    @Configuration // 스프링부트가 설정 정보를 함께 로딩함
    public class SecurityConfig extends WebSecurityConfigurerAdapter {
        @Autowired
        public void configureGlobal(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception{
            authenticationManagerBuilder.inMemoryAuthentication()
                    .withUser("yunhwan")
                    .password("{noop}mypassword1")
                    .roles("USER");
            // USER 권한 <윤환> 비밀번호 mypassword (noop) 인코딩 x
        }
    }
    ```

## Java Persistence API 개요

### JPA

- 자바 ORM 기술에 대한 표준 명세
- 자바 어플리케이션에서 관계형 데이터베이스를 사용하는 방식
을 정의한 `인터페이스`
- EntityManager를 통해 CRUD를 처리함

### Hibernate

- JPA 인터페이스를 구현한 `구현체`
- JAVA ORM을 위한 라이브러리

### Spring Data JPA

- 스프링 모듈
- 개발자가 JPA를 좀 더 사용하기 쉽게 도와줌
- JPA를 `추상화`한 Repository 인터페이스 제공

- 구성 그림

    ![README%20md%20ba4c33a880bb41809acbf0d18c4a918d/_2021-01-07__4.13.53.png](README%20md%20ba4c33a880bb41809acbf0d18c4a918d/_2021-01-07__4.13.53.png)

CRUD를 공통된 인터페이스로 처리할 수 있으므로
개발자는 더욱 비즈니스 로직에만 집중해서 구현할 수 있다.

## JPA 사용을 위한 Dependency 추가 및 설정

- 디펜던시 추가 (jpa & h2 database)

    ```xml
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>

    <dependency>
        <groupId>com.h2database</groupId>
        <artifactId>h2</artifactId>
        <scope>runtime</scope>
    </dependency>
    ```

- application.yml 파일
    - 사용할 datasource를 추가해줘야만 한다.

    ```yaml
    spring:
      . . .
      datasource:
        url: jdbc:h2:mem:testdb
      jpa:
        show-sql: true # 디버그 용
      h2:
        console:
          enabled: true
    ```

Security 인증을 무시하려면  SecurityConfig에서 따로 설정을 해줘야만 한다.

## Spring Data JPA를 이용한 Entity 설정과 초기 데이터 생성

- `@Entity` 어노테이션
    - 클래스 명은 테이블 이름
    - 필드는 컬럼이 된다.
- `@Id` 어노테이션
    - 레코드를 구분하기 위한 주 키
    - `@GeneratedValue` : 자동 생성될 수 있는 값

데이터베이스의 테이블로 생성해줌

- 테이블 생성 (User 도메인)

```java
@Entity
public class User {
    @Id
    @GeneratedValue
    private Integer id;


    private String name;
    private Date joinDate;

    private String password;
    private String ssn;
}
```

- 서버 구동 시 실행되는 쿼리 (초기 데이터 생성)
    - 클래스를 테이블로 생성함

```sql
// => 서버 처음 구동 시 실행 쿼리
Hibernate: drop table if exists user CASCADE
Hibernate: drop sequence if exists hibernate_sequence
Hibernate: create sequence hibernate_sequence start with 1 increment by 1
Hibernate: create table user (id integer not null, join_date timestamp, name varchar(255), password varchar(255), ssn varchar(255), primary key (id))
```

### resources 폴더에 data.sql 추가하기

data.sql 파일에 쿼리를 추가하면 서버 처음 구동 시 파일의 쿼리를 실행한다.

- 서버 구동 직후 데이터베이스

![README%20md%20ba4c33a880bb41809acbf0d18c4a918d/_2021-01-07__5.07.44.png](README%20md%20ba4c33a880bb41809acbf0d18c4a918d/_2021-01-07__5.07.44.png)

## JPA Service 구현을 위한 Controller, Repository 생성

- User entity를 사용하기 위한 메서드
- JpaRepository를 만들어야 함(Spring Data JPA를 사용 예정)

### UserRepositroy 인터페이스 extends JpaRepository

- 기본적인 CRUD가 구현되어져 있다.
- 필요한 기능은 구현하면 된다.
- `JpaRepository<Entity, Entity의 주 키>` 지정

```java
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

}
```

## JPA를 이용한 사용자 목록 조회
GET HTTP Method

### JpaController

- `@RequestMapping` : 공통 주소 지정
- JpaRepository에서 구현한 메서드 사용
    - retriveAllUsers()

        ```java
        @RestController
        @RequestMapping("/jpa")
        public class UserJpaController {
            @Autowired
            private UserRepository userRepository;

            @GetMapping("/users")
            public List<User> retrieveAllUsers() {
                return userRepository.findAll();
            }
        }
        ```

    - retrieveUser()
        - Optional 리턴인 이유는 데이터가 `id`에 따라 존재할수도있고 아닐수도 있기에 선택적으로 반환하기 위해서이다.

        ```java
        @GetMapping("/users/{id}")
            public User retrieveUser(@PathVariable int id) {
                Optional<User> user = userRepository.findById(id);

                if (!user.isPresent()) {
                    throw new UserNotFoundException(String.format("ID[%s] not found", id));
                }
                return user.get();
            }
        ```

## JPA를 이용한 사용자 추가 & 삭제
POST/DELETE HTTP Method

반드시 JpaRepository를 사용하는 Repo를 사용

- Delete HTTP Method
    - `@pathvariable` 가변 uri
    - 구현되어져 있는 메서드 사용 (기본 CRUD)

    ```java
    @DeleteMapping("/users/{id}")
        public void deleteUser(@PathVariable int id) {
            userRepository.deleteById(id);
        }
    ```

- Post HTTP Method
    - 추가할 User 정보가 Body에 들어가므로 `@RequestBody` 어노테이션 사용
    - `@Valid` 인자가 유효한지 검사

    ```java
    @PostMapping("/users")
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        User savedUser = userRepository.save(user);
        // 지금 생성된 데이터 ID를 지정
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedUser.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }
    ```

    중간에 `URI ~~ 코드`는 어느 URI를 요청하면 해당 자원에 접근할 수 있는지 헤더의 `Location` 에 넣어준다.

`@GeneratedValue` 는 시퀀스 값을 가지고 동작하는데 이 시퀀스 값과 미리 초기화 된 값이 겹치면 서버 오류가 발생한다.

## 게시물 관리를 위한 Post Entity 추가 및 초기 데이터 생성

- User와 Post의 관계 (1 대 다 관계)

![README%20md%20ba4c33a880bb41809acbf0d18c4a918d/_2021-01-07__8.11.20.png](README%20md%20ba4c33a880bb41809acbf0d18c4a918d/_2021-01-07__8.11.20.png)

- Post Entity 추가

    ```java
    @Entity // jpa
    @Data // lombok setter getter tostring
    @AllArgsConstructor
    @NoArgsConstructor
    public class Post {

        @Id
        @GeneratedValue
        private Integer id; // 기본 키

        private String description; // 내용

        // User : Post =관계= 1 : (0 ~ N), Main : sub (Parent(주) : Child(부))
        // Post 엔티티 입장에선 유저 데이터가 하나이다. 유저 입장에서 Post가 여러 개일 수 있다.
        // LAZY 지연 로딩 방식 : 사용자 데이터를 가져올 때 매번 Post를 가져오는 것이 아닌 Post를 가져올 때만 User 데이터를 가져옴
        @ManyToOne(fetch = FetchType.LAZY)
        @JsonIgnore //해당 필드는 외부에 데이터를 노출하지 않음
        private User user; //작성자
    }
    ```

- User Entity 수정

    ```java
    @OneToMany(mappedBy = "user") // user테이블의 데이터와 매핑이 된다. (1 대 다) ==> 지도교수 : 사원
        private List<Post> posts;
    ```

data.sql을 통해 초기화를 진행한다.

## 게시물 조회를 위한 Post Entity와 User Entity 관계 설정

- 사용자의 모든 Post 조회
    - User는 Optional을 통한 예외처리
    - user를 찾아서 user의 모든 post 반환 (json 형태)

    ```java
    @GetMapping("/users/{id}/posts")
        public List<Post> retrieveAllPostsByUsers(@PathVariable int id) {
            Optional<User> user = userRepository.findById(id); //User는 Optional로 받아온다.

            if (!user.isPresent()) {
                throw new UserNotFoundException(String.format("ID[%s] not found", id));
            }

            return user.get().getPosts(); //user가 가지고 있는 post를 반환
        }
    ```

    - Post에 대한 Data만 반환

        ```json
        [
            {
                "id": 10001,
                "description": "My first post"
            },
            {
                "id": 10002,
                "description": "My second post"
            }
        ]
        ```

    만약 유저 데이터가 없다면 오류 발생 메시지를 반환한다.

## JPA를 이용한 새 게시물 추가
POST HTTP Method

- Post에 접근하기 위해 PostRepository 생성

    ```java
    @Repository
    public interface PostRepositroy extends JpaRepository<Post, Integer> {

    }
    ```

- Post 생성 API

    ```java
    @PostMapping("/users/{id}/posts")
    public ResponseEntity<Post> createPost(@PathVariable int id, @RequestBody Post post) {

        //사용자 데이터 가져오는 부분 복사
        Optional<User> user = userRepository.findById(id); //User는 Optional로 받아온다.

        if (!user.isPresent()) {
            throw new UserNotFoundException(String.format("ID[%s] not found", id));
        }

        post.setUser(user.get()); // post에 무조건 User정보를 채워줘야한다ㅏ.
        Post savedPost = postRepositroy.save(post); // 바로하면 안되고 User정보 필요
        // 지금 생성된 데이터 ID를 지정
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}") // user id가 아닌 post id 이다!!
                .buildAndExpand(savedPost.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }
    ```

    1. User의 정보를 가져와서 Post에 넣어준다.
    2. Jpa 기본 CRUD를 이용해 post를 저장한다.
    3. 저장한 Post를 GET할 수 있는 URI를 알려준다.

    # Richardson Maturity Model (성숙도 모델)

    리차드슨 - 레스트풀 설계한 분

    - Lv0
        - 기존의 웹서비스를 URI 형태로 리소스 제공
            - /getPosts, /deletePosts, /doThis ⇒ 동작이나 상태값을 URI에 같이 표시함
    - Lv1
        - 의미있고 적절한 URI를 사용하기 시작
            - http method에 따라 적절히 URI를 제공하진 않음
    - Lv2
        - Lv1 + HTTP Method 추가
        - GET, POST, PUT, DELETE Method 사용
        - 같은 URI라고 하더라도 Method에 따라 다른 기능을 할수 있게됨
    - Lv3
        - Lv2 + HATEOAS ⇒ 추가적인 정보를 제공
        - URI를 입력하면 다음에 사용해야할 URI 또한 알수 있다.

    # RESTful 서비스 설계 시 주의사항

    - 소비자 입장
        - 간단명료하며 직관적인 URI
        - 최소한 Lv2의 Reqeust Method 사용할 것
        - 각 API요청에 따른 적절한 상태 반환 값을 돌려줄 것 (Response Status, 200, 404, 400, 201, 401)
        - URI에 Secure한 정보를 사용하면 안됨
        - URI명은 단수, 복수를 구분할 것 동사보단 명사를 사용할 것
        - 일관된 접근 URI 가질 것 (PUT, DELETE /gist/{id}/star)
