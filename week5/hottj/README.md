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
