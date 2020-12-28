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

✅ <동작과정><br>
웹 브라우저에서 url 요청 -> 내장 웹 서버 톰캣이 스프링에게 url 전달 -> hello-static과 맵핑된 controller가 없다 -> 스프링 부트가 resource에서 hello-static 찾아서 return

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
View
```html
<html xmlns:th="http://www.thymeleaf.org">
<body>
   <p th:text="'hello '+ ${name}">hello! empty</p>
</body>
</html>  
```
![image](https://user-images.githubusercontent.com/46257667/103173536-0ec93780-489f-11eb-8257-4acf1a810869.png)

✅ <동작과정><br>
웹 브라우저에서 url 요청 -> 내장 웹 서버 톰캣이 스프링에게 url 전달 -> 스프링은 return값과 model을 viewResolver에게 전달 -> viewResolver가 thymeleaf 템플릿 엔진과 매핑 -> thymeleaf 템플릿 엔진이 넘어온 값들 처리 후 랜더링 -> view에 전달

#### 🔶 API
ResponseBody 문자 반환
```java
@Controller
public class HelloController {
   @GetMapping("hello-string")
   @ResponseBody
   public String helloString(@RequestParam("name") String name) {
      return "hello " + name;
 }
}
```
* ResponseBody 어노테이션 사용하면 viewResolver를 사용하지 않음
* HTTP body 부분에 문자 내용을 직접 반환
ResponseBody 객체 반환
```java
@Controller
public class HelloController {
   @GetMapping("hello-api")
   @ResponseBody
   public Hello helloApi(@RequestParam("name") String name) {
      Hello hello = new Hello();
      hello.setName(name);
      return hello;
   }
   static class Hello {
      private String name;
      public String getName() {
         return name;
      }
      public void setName(String name) {
         this.name = name;
      }
   }
}
```
![image](https://user-images.githubusercontent.com/46257667/103208494-1e4e8c00-4944-11eb-9976-48958ac72f77.png)
