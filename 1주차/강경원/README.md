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
  * spring-test : 스플이 통합 테스트 지원
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
  * 정적 컨텐츠는 그대로 반환된다.
