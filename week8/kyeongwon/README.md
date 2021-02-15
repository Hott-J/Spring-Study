## :cherry_blossom: 처음 배우는 스프링 부트2 - Chapter 6 스프링 부트 데이터 REST

### 1. 배경지식
* REST
  * 웹과 같은 분산 하이퍼미디어 시스템에서 사용하는 통신 네트워크 아키텍처
  
  * REST를 사용하는 목적
    * 구성요소 상호작용의 규모 확장성
    * 인터페이스의 범용성
    * 구성요소의 독립적인 배포
    * 중간적 구성요소를 이용한 응답 지연 감소, 보안 강화, 레거시 시스템 인캡슐레이션
  
  * REST API 구성
    * 자원(resource) : URI
    * 행위(verb) : HTTP 메소드 - GET, POST, PUT, DELETE
    * 표현(REPRESENTATIONS) : 리소스에 대한 표현(HTTP Message Body)

### 2. REST API 설계
* MVC 패턴을 활용할 경우
  * Controller, Service, Repository로 나누어 데이터의 운반 처리를 조작
  
* Spring Boot Date REST을 활용할 경우
  * Repository 하나만 생성하면 된다.
  * Spring Boot Data REST는 URL 요청을 리포지토리 내부의 CRUD method와 매핑하여 처리한다.   
  <img src="https://user-images.githubusercontent.com/61045469/107645464-0ea0c200-6cbc-11eb-88d5-f1ff6e45b591.png" width="60%" height="40%"></img><br/>
    
### 3. 스프링 부트 MVC 패턴으로 REST API 구현하기
* H2대신 MySQL과 연동
  * Spring-Boot-Community-Rest, Spring-Boot-Community-Web의 build.gradle에 의존성 추가
  ```java
  runtime('mysql:mysql-connector-java')
  ```

  * /resources/application.yml에 MySQL을 사용하도록 설정
  ```java
  spring:
    datasource:
      url: jdbc:mysql://127.0.0.1:3306/MyDB?useSSL=false
      username: root
      password: root
      driver-class-name: com.mysql.jdbc.Driver
  ```

* Spring-Boot-Community-Rest 프로젝트 구성
  * Spring-Boot-Community-Rest/rest-web : MVC 패턴
  * Spring-Boot-Community-Rest/data-rest : 데이터 레스트 방식

* 커뮤니티 게시판(Spring-Boot-Community-Web) - 8080 포트로 구현
  * REST API와 연동하기 위해 /resources/templates/board/form.html에 자바스크립트 통신용 코드 구현
  * 게시판 글 생성, 수정, 삭제에 대한 요청을 Ajax로 구현하여 비동기로 서버와 통신할 수 있다.

* REST API(Spring-Boot-Community-Rest/rest-web) - 8081 포트로 구현
  * /domain/enums/BoardType.java, /domain/enums/SocialType.java
  * /domain/Board.java, /domain/User.java
  * /repository/BoardRepository.java, /repository/UserRepository.java
  * /controller/BoardRestController.java

  * 애플리케이션을 구동시킨 후 [http://localhost:8081/api/boards](http://localhost:8081/api/boards)에 접속
    * REST API에서 페이징 처리된 데이터 확인
    * _embedded : 호출한 목적 데이터가 중첩 형식으로 부여
    * _links : 관련된 링크들
    * _page : 페이징 처리를 위한 값들

* CORS 허용 및 시큐리티 설정
  * localhost:8080과 REST API 프로젝트의 localhost:8081은 포트가 다르기 때문에 Ajax요청이 모두 실패하게 된다.
  * 교차 출처 HTTP 요청을 가능하게 해주는 메커니즘을 CORS(cross origin resource sharing)이라고 한다.
  * CORS는 서로 다른 도메인의 접근을 허용하는 권한을 부여한다.
  * 스프링 시큐리티를 이용하여 CORS 설정 및 적용
  ```java
  @SpringBootApplication
  public class SpringBootCommunityRestApplication {

      public static void main(String[] args) {
          SpringApplication.run(SpringBootCommunityRestApplication.class, args);
      }

      @Configuration
      @EnableGlobalMethodSecurity(prePostEnabled = true)
      @EnableWebSecurity // 웹용 시큐리티를 활성화
      static class SecurityConfiguration extends WebSecurityConfigurerAdapter {

          @Override
          protected void configure(HttpSecurity http) throws Exception {
              CorsConfiguration configuration = new CorsConfiguration();
              // CorsConfiguration 객체를 생성하여 CORS에서 origin, method, header별로 혀용할 값을 설정
              configuration.addAllowedOrigin("*"); // "*"은 CorsConfiguration.ALL로, 모든 경로에 대해 허용함을 의미
              configuration.addAllowedMethod("*");
              configuration.addAllowedHeader("*");
              UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
              source.registerCorsConfiguration("/**", configuration);

              http.httpBasic()
                  .and().authorizeRequests()
                  .anyRequest().permitAll()
                  .and().cors().configurationSource(source)
                  .and().csrf().disable();
          }
      }
  }
  ```

* /controller/BoardRestController.java, /domain/Board.java에 생성, 수정, 삭제 메소드 구현

* 동작 확인
  * API는 8081포트로, 커뮤니티 게시판은 8080포트로 실행한 후 기능 확인

### 4. 스프링 부트 데이터 레스트로 REST API 구현하기
* REST API(Spring-Boot-Community-Rest/data-rest) 프로젝트
  * /resources/application.yml에 MySQL을 사용하도록 설정
  ```java
  spring:
    datasource:
      url: jdbc:mysql://127.0.0.1:3306/MyDB
      username: root
      password: root
      driver-class-name: com.mysql.jdbc.Driver
    data:
      rest:
        base-path: /api # API의 모든 요청의 기본 경로 지정
        default-page-size: 10 # 클라이언트가 따로 페이지 크기를 요청하지 않았을 때 적용할 기본 페이지 크기 설정
        max-page-size: 10 # 최대 페이지 수 설정
  server:
    port: 8081
  ```

* REST API(Spring-Boot-Community-Rest/data-rest) - 8081 포트로 구현
  * @RepositoryRestResource : 스프링 부트 데이터 레스트에서 지원하는 어노테이션으로, 별도의 Controller와 Service 없이 미리 내부적으로 정의되어 있는 로직을 따라 처리된다.

  * @RepositoryRestController : projection을 적용하여 원하는 필드만 노출시킬 수 있다.

  * /domain/projection/UserOnlyContainName.java
  ```java
  // User 이름만 노출하도록 설정
  @Projection(name="getOnlyName", types={User.class})
  public interface UserOnlyContainName {

      String getName();
  }
  ```

  * /repository/UserRepository.java
  ```java
  @RepositoryRestResource(excerptProjection = UserOnlyContainName.class)
  public interface UserRepository extends JpaRepository<User, Long> {
  }
  ```

* 이벤트 바인딩
  * 여러 이벤트 발생 시점을 가로채서 원하는 데이터를 추가하거나 검사하는 이벤트 어노테이션 제공
  * @RepositoryEventHandler : BeforePostProcessor에 클래스가 검사될 필요가 있다.
  * @RepositoryEventHandler 선언 후 밑의 어노테이션 사용
    * @HandleBeforeCreate : 생성하기 전의 이벤트
    * @HandleBeforeSave : 수정하기 전의 이벤트
    * @HandleBeforeDelete : 삭제하기 전의 이벤트
    * 이 외에도 7개의 어노테이션이 있다.

<br/>

## :cherry_blossom: 처음 배우는 스프링 부트2 - Chapter 7 스프링 부트 배치

### 1. 배경지식
* 스프링 부트 배치를 활용하여 오랫동안 접속하지 않은 휴먼회원 전환 및 관리하기

* 스프링 부트 배치의 장점
  * 대용량 데이터 처리에 최적화되어 고성능을 발휘한다.
  * 효과적인 로깅, 통계 처리, 트랜잭션 관리 등 재사용 가능한 필수 기능을 지원한다.
  * 수동으로 처리하지 않도록 자동화되어 있다.
  * 예외사항, 비정상 동작에 대한 방어 기능이 있다.
  * 스프링 부트에서 배치 처리 기능을 제공하므로 개발자는 비즈니스 로직에 집중할 수 있다.

* 배치 처리(읽기 -> 처리 -> 쓰기)
  * 읽기(read) : 데이터 저장소(데이터베이스)에서 특정 데이터 레코드를 읽는다.
  * 처리(processing) : 원하는 방식으로 데이터를 가공/처리한다.
  * 쓰기(write) : 수정된 데이터를 다시 저장소(데이터베이스)에 저장한다.   
  <img src="https://user-images.githubusercontent.com/61045469/107879128-66d4ff80-6f1a-11eb-932a-de94df265cdb.png" width="60%" height="40%"></img><br/>

* 청크 지향 프로세싱
  * Job
    * 배치 처리 과정을 하나의 단위로 만들어 표현한 객체
    * 전체 배치 처리에 있어 항상 최상단 계층에 있다.
    * 하나의 Jop에는 여러 Step이 있다.
    * JobBuilderFactory로 원하는 Jop을 만들 수 있다.

    * JobInstance
      * 배치에서 Job이 실행될 때 하나의 Job 실행 단위
      * JobInstance는 여러 개의 JobExecution을 가질 수 있다.

    * JobExecution
      * JobInstance에 대한 한 번의 실행을 나타내는 객체

    * JobParameters
      * Job이 실행될 때 필요한 파라미터들을 Map 타입으로 저장하는 객체
      * JobInstance를 구분하는 기준이 되기도 한다.

  * Step
    * 실질적인 배치 처리를 정의하고 제어하는 데 필요한 모든 정보가 들어 있는 도메인 객체
    * Job을 처리하는 실질적인 단위로 사용된다.
    * 모든 Job에는 1개 이상의 Step이 있어야 한다.

    * StepExecution
      * Step실행 정보를 담는 객체
      * Step이 실행될 때마다 StopExecution이 생성된다.

  * JobRepository
    * 배치 처리 정보를 담고 있는 메커니즘
    * 어떤 Job이 실행되었으며 몇 번 실행되었는지, 언제 끝나는지 등의 배치 처리에 대한 메타데이터를 저장한다.
    * Step의 실행 정보를 담고 있는 StepExecution도 저장소에 저장하며 전체 메타데이터를 저장/관리하는 역할을 수행한다.

  * JobLauncher
    * 배치를 실행시키는 인터페이스   

  * ItemReader
    * Step의 대상이 되는 배치 데이터를 읽어오는 인터페이스
    * FILE, XML, DB 등 여러 타입의 데이터를 읽어올 수 있다.

  * ItemProcessor
    * ItemReader로 읽어온 배치 데이터를 변환하는 역할을 수행한다.
    * 로직 처리만 수행(저장은 ItemWriter가 수행)

  * ItemWriter
    * 배치 데이터를 DB나 파일에 저장한다.   
    <img src="https://user-images.githubusercontent.com/61045469/107948440-2a1b0e00-6fd7-11eb-818e-d495fac7b7da.png" width="60%" height="40%"></img><br/>

* /resources/import.sql : 하이버네이트, /resources/data.sql : 스프링 JDBC가 실행