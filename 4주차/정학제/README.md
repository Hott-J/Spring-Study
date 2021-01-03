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

  
  
