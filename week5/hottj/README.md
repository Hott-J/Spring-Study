# :cherry_blossom: Spring boot를 이용한 RESTful Web Services 개발

---

## :four: RESTful Service 기능 확장

### :smile: Section 3 수업 소개

- 유효성 검사
- 다국어 지원
- XML 포멧
- 필터링
- 버젼 관리

### :smile: 유효성 체크를 위한 Validation API 사용
- [참고](https://mangkyu.tistory.com/72)

### :smile: 다국어 처리를 위한 Internationalization 구현 방법
- [참고](https://spiralmoon.tistory.com/entry/Spring-boot-Spring-boot%EC%97%90%EC%84%9C-%EB%8B%A4%EA%B5%AD%EC%96%B4-%EC%A7%80%EC%9B%90%ED%95%98%EA%B8%B0-1?category=790800)

### :smile: Response 데이터 형식 반환 - XML format
- pom.xml에 XML format을 사용하기 위한 dependency 추가

### :smile: Response 데이터 제어를 위한 Filtering
- @JsonIgnoreProperties / @JsonIgnore 애노테이션 사용

### :smile: 프로그래밍으로 제어하는 Filtering 방법 - 개별 사용자 조회
- [참고](https://pooney.tistory.com/69)

### :smile: 프로그래밍으로 제어하는 Filtering 방법 - 전체 사용자 조회
- [참고](https://pooney.tistory.com/69)

### :smile: URI를 이용한 REST API Version 관리
- @GepMapping("v1/users/{id}")
- [참고](https://gompangs.tistory.com/entry/JAVASpring-BeanUtils-%EA%B4%80%EB%A0%A8)

### :smile: Request Parameter와 Header를 이용한 API Version 관리
- Request Paramter
  - @GetMapping(value="/users/{id}/",params="version=1")
    - value의 맨뒤에 / 가 있어야 버젼 정보가 추가로 뒤에 들어간다.
    - `http://localhost:8088/admin/users/1/?version=1` 와 같은 방식으로 요청. 파라미터 부분은 ?를 넣어준다.
- Header
  - @GetMapping(value="/users/{id}/", headers="X-API-VERSION=1)
    - 헤더 값은 임의로
    - KEY = X-API-VERSION , VALUE = 1
- MIME
  - @GetMapping(value="/users/{id}/",produces="application/vnd.company.appv1+json")
    - produces 값 임의로. 마지막에 +반환타입 넣어준다.
    - KEY = Accept , VALUE = application/vnd.company.appv1+json

## :five: Spring Boot API 사용

### :smile: Level3 단계의 REST API 구현을 위한 HATEOAS 적용
- 스프링 버젼에 따라 Resource / EntityModel 등 구현 방식이 다르다.
- HATEOAS 를 통해 사용자가 접근할 수 있는 정보를 더 많이 줌으로써 편리성을 제공

### :smile: REST API Documentation을 위한 Swagger 사용
- 내가 만든 정보들을 가지고 보기 좋게 만들어준다.
- `localhost:8088/v2/api-docs` 를 요청값으로 보내면 내가 만든 정보들이 나온다.(JSON / XML)

### :smile: Swagger Documentation 구현 방법
- [참고](https://springboot.tistory.com/24)

![캡처1](https://user-images.githubusercontent.com/47052106/103926980-05708280-515d-11eb-8f99-066ab656e1d8.JPG)
![캡처2](https://user-images.githubusercontent.com/47052106/103926975-03a6bf00-515d-11eb-9473-12127e36d2d5.JPG)
![캡처3](https://user-images.githubusercontent.com/47052106/103926977-04d7ec00-515d-11eb-91df-a8214847c9d3.JPG)

### :smile: REST API Monitoring을 위한 Actuator 설정
- dependency와 yml 파일을 수정한다.

### :smile: HAL Browser를 이용한 HATEOAS 기능 구현
- 스프링 2.3.7 릴리즈 버젼일 경우
  - Swagger2 와 Swagger-Ui 버젼을 2.9.2 가 아니라 3.0.0 으로 수정하여야 컴파일됩니다.
    - 수정시, `localhost:8088/swagger-ui.html#/` 이 동작하질 않습니다.

### :smile: Spring Security를 이용한 인증 처리
- 디펜덴시 추가
- 콘솔창에서 password 찾아서 확인. username은 user. 
  - postman에서 authorization 탭에서 type을 basic auth 로 설정해 줄 것.
  
### :smile: Configuration 클래스를 이용한 사용자 인증 처리
- WebSecurityConfigurerAdapter를 상속받아 생성한 Configuration 빈에서 처리한 인증 정보가 application.yml에서 설정한 것보다 우선순위가 높게 스프링이 처리하는 것인가요?
  - 우선순위는 application.yml 파일보다 직접 구현한 WebSecurityConfiguerAdapter가 높습니다. 
  
- WebSecurityConfigurerAdapter 상속받은 Config 빈은 반드시 1개로 고유해야 하나요? 궁금해서 또 하나 만들어서 같은 인증 처리 해주니 "WebSecurityConfigurers must be unique" 로그가 찍히면서 에러 난다.
  - Spring4에서는 부터는 @Order 어노테이션을 이용해서 같은 타입의 빈이 Autowired 될 때 순서를 지정할 수 있습니다. WebSecurityConfiguerAdapter는 @Order(100)이라는 우선순위로 선언되어 있는데, 만약 WebSecurityConfiguerAdapter 클래스를 상속받은 SecurityConfig2라는 클래스를 선언했다고 가정하면, SecurityConfig2 클래스 역시 @Order(100)라는 같은 우선순위를 갖게 되기 때문에, 오류가 발생합니다. 해결하는 방법은 SecurotyConfig2 클래스 선언 시 명시적으로 @Order(200)과 같은 값으로 선언하게 되면(적은 값이 높은 우선순위), 같은 타입의 빈이라 하더라도, 우선순위가 다르기 때문에, 오류가 발생하지 않습니다. 
```java
@Configuration
@Order(200)
public class SecurityConfig2 extends WebSecurityConfigurerAdapter {
...
```

## :six: Java Persistence API 사용

### :smile: JPA를 사용을 위한 Dependency 추가와 설정
- h2 버젼 1.4.197 로 설정할 것.

### :smile: Spring Data JPA를 이용한 Entity 설정과 초기 데이터 생성
- yml 파일에 `spring:datasource:url:jdbc:h2:mem:testdb` 을 추가할 것.

### :smile: JPA Service 구현을 위한 Controller, Repository 생성
- JpaRepository 를 상속하여 인터페이스를 만든다.

### :smile: JPA를 이용한 사용자 목록 조회 - GET HTTP Method
- 개별 조회의 경우, 존재할지 안할지 모르므로 Optinal 객체로 감싸야한다. 
- 헤테오스 기능을 이용하여 개별 조회여도 전체 조회할 수 있는 링크도 걸어둔다.

### :smile: JPA를 이용한 사용자 추가와 삭제 - POST/DELETE HTTP Method
- ID 값은 자동적으로 초기값이 1이고 1씩 증가해서 생성해준다. 따라서 기본으로 들어가는 데이터의 ID값이 1,2,3 이라면, POST 메소드 요청시 1부터 ID값을 자동적으로 할당하기에 1이 중복이 되므로 오류가 발생한다. 따라서 기본 데이터의 ID값을 9991와 같이 크게 만들어놓고 테스트 진행하였다.
- postman 사용시, POST 요청일 경우, Body -> raw 에 JSON 값을 넣고, 이는 GraphQL 오른편에 JSON으로 선택해서 요청을 보내야한다.
