# Spirng boot 첫 프로젝트
----------------
### 1️⃣ 라이브러리 살펴보기
#### 🔶 스프링 부트 라이브러리
 * spring-boot-starter-web
    * spring-boot-starter-tomcat : 톰캣(웹서버)
    * spring-webmvc : 스프링 웹 MVC
 * spring-boot-starter-thymeleaf : 타임리프 템플릿 엔진(View)
 * spring-boot-starter(공통) : 스프링부트 + 스프링 코어 + 로깅
    * spring-boot
      * spring-core
    * spring-boot-starter-logging
      * logback, slf4j
### 2️⃣ 스프링 웹 개발 기초
#### 🔶 정적 컨텐츠
![image](https://user-images.githubusercontent.com/46257667/103171993-914bfa00-4893-11eb-9d31-047c98f15161.png)

✅ 웹 브라우저에서 url 요청 -> 내장 웹 서버 톰캣이 스프링에게 url 전달 -> hello-static과 맵핑된 controller가 없다 -> 스프링 부트가 resource에서 hello-static 찾아서 return

#### 🔶 MVC와 템플릿 엔진
Controller
```java
@Controller
public class HelloController{
   @GetMapping("hello-mvc")
   public String helloMVC(@RequestParam("name") String name, Model model) {
      model.addAttribute("name", name);
      return "hello-template";
   }
}
```
