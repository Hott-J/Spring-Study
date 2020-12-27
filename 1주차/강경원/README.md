## :cherry_blossom: Spring 기초

### 1. Spring 환경 설정
* Java 11, IntelliJ 설치

### 2. Spring 프로젝트 설정
* [https://start.spring.io](https://start.spring.io/) 사이트 접속하여 프로젝트 생성   
* 프로젝트 옵션
  * Project : Gradle Project
  * Language : Java
  * Sprint Boot : 2.4.1
  * Project Metadata
    * Group : hello
    * Artifact : hello-spring
  * Dependencies : Spring Web, Thymeleaf(html 만들어주는 템플릿 엔진)

### 3. 라이브러리 의존성
* spring-boot-starter-web
  * spring-boot-starter-tomcat : 톰캣(웹서버)
  * spring-webmvc : 스프링 웹 MVC
* spring-boot-starter-thymeleaf : 타임리프 템플릿 엔진(View)
* spring-boot-saarter(공통) : 스프링 부트 + 스프링 코어 + 로깅
  * spring-boot
    * spring-core
  * spring-boot-starter-logging
    * logback, slf4j
* spring-boot-starter-test
  * junit : 테스트 프레임워크
  * spring-test : 스프링 통합 테스트 지원
  * mockito
  * assertj

### 4. View 환경설정
* Welcome Page 기능
  * resources/static/에 index.html 파일을 올려두면 스프링 부트에서 Welcome page 기능을 제공한다.
* Thymeleaf 템플릿 엔진 사용
<img src="https://user-images.githubusercontent.com/61045469/103153029-ad3a9780-47d0-11eb-8181-14e4f38ca73d.PNG" width="90%" height="70%"></img><br/>
  * GetMapping annotation : HTTP GET 요청을 처리하는 method (HTTP POST 요청 처리는 PostMapping annotation)
  * return값 : Controller에서 return값으로 문자를 반환하면 viewResolver가 resources/templates 내에 있는 html파일명과 return값을 매칭시켜서 처리한다.
  
### 5. 빌드하고 실행
* 실행 환경 : Windows
* 콘솔로 이동하여 명령어 입력
  * gradlew build
  * cd build/libs
  * java -jar hello-spring-0.0.1-SNAPSHOT.jar
  * 실행 확인 (다시 build하고 싶을 경우 : gradlew clean build)

### 6. 스프링 웹 개발 기초
* 정적 컨텐츠   
<img src="https://user-images.githubusercontent.com/61045469/103153554-ba598580-47d4-11eb-85ff-448b981087dd.PNG" width="90%" height="70%"></img><br/>
  * 정적 컨텐츠는 파일 그대로 반환된다.
* MVC와 템플릿 엔진  
<img src="https://user-images.githubusercontent.com/61045469/103170901-462de900-488b-11eb-89c0-e2cd73d859e1.PNG" width="90%" height="70%"></img><br/>
  * model -> name은 key, spring은 value
  * 정적 컨텐츠와는 달리 Thymeleaf 템플릿 엔진이 html 파일을 rendering해서 반환한다.
  * 템플릿 엔진을 MVC 각각으로 쪼개서 rendering된 파일을 반환하는 방식이다.
  * MVC : Model, View, Controller (View : 화면을 그리는 역할, Model Controller : business logic, server backend 처리 담당)
  * Thymeleaf 장점 : server없이 파일을 열어서 볼 수 있다.
* API   
<img src="https://user-images.githubusercontent.com/61045469/103171584-c0ad3780-4890-11eb-984f-4764e679ad64.PNG" width="90%" height="70%"></img><br/>
  * ResponseBody annotation을 발견하면 viewResolver에게 요청하지 않는다.
  * viewResolver 대신 HttpMessageConverter가 동작
    * 문자열일 경우 : 문자열 데이터를 HTTP응답으로 넣어서 그대로 반환한다. -> String Converter가 동작
    * 객체일 경우 : 객체를 json 방식으로 데이터를 만들어서 HTTP응답으로 넣어 반환한다. -> Json Converter가 동작
    * 그 외 여러 포멧에 대한 HTTPMessageConverter도 등록되어 있다.
    * 보통은 객체 반환을 위주로 사용된다.
<br/>

## :cherry_blossom: 회원 관리 예제
